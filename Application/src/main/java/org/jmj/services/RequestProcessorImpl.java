package org.jmj.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jmj.constants.Constants;
import org.jmj.entity.*;
import org.jmj.repository.RequestRepository;
import org.jmj.repository.ResponseRepository;
import org.jmj.repository.SubsystemRepository;
import org.jmj.services.caching.PathCache;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.jmj.entity.ResponseType.REST;

@Slf4j
@Service
@Data
public class RequestProcessorImpl implements RequestProcessor {
    private final ResponseRepository responseRepository;
    private final SubsystemRepository subsystemRepository;
    private final RequestRepository requestRepository;
    private final AzPublisherService azPublisherService;
    private final PathCache pathCache;
    //Todo: Genearte Rest response based for subsystem based on open api json

    @Override
    public ResponseEntity<String> processRequest(ServerHttpRequest req, HttpHeaders headers, SubSystem subSystem) {
        //Path has the subsystem in it, so its removed
        String path = java.net.URLDecoder.decode(String.valueOf(req.getPath().subPath(2)), StandardCharsets.UTF_8);

        log.info("Request received: {} {}", path, req.getMethod());
        //to get both the configured request path and also set context
        var actRqstPthAndCtxt=pathCache.search(subSystem.getName(), path);
        RequestId requestId = getRequestId(subSystem.getName(), req.getMethod().name(), actRqstPthAndCtxt.path());
        List<Response> responses = responseRepository.findByRequest_IdAndStatusCodeOrderByType(
                getRequestId(subSystem.getName(), req.getMethod().name(), actRqstPthAndCtxt.path()),
                actRqstPthAndCtxt.getStatus(req.getMethod())
        );
        if (responses.isEmpty())
            return ResponseEntity.status(actRqstPthAndCtxt.getStatus(req.getMethod())).body(Constants.NO_CONFIG_MSG);

        processAsyncResponses(responses, actRqstPthAndCtxt.context());

        return responses.stream()
                .filter(response -> response.getType() == REST)
                .findFirst()
                .map(response -> {
                    log.info("Response found: {}", response.getBody());
                    return resolveResponseBody(response, actRqstPthAndCtxt.context());
                })
                //If no rest response it means some async request was present
                .orElseGet(() -> ResponseEntity.accepted().body(Constants.ONLY_ASYNC_RESPONSES_MSG));

    }

    private ResponseEntity<String> resolveResponseBody(Response response, Map<String, String> context){
        String responseBody = response.getBody();
        for (Map.Entry<String, String> entry : context.entrySet()) {
            responseBody = responseBody.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return ResponseEntity.status(response.getStatusCode()).body(responseBody);
    }
    @Override
    public String updateStatusForRequest(RequestId requestId,HttpStatus status){
        pathCache.updateStatus(requestId.getSubSystemId(),requestId.getPath(),requestId.getMethod(),status);
        return "Updated";
    }


    @Async
    private void processAsyncResponses(List<Response> responses, Map<String, String> context){
        //response processed in order of response order
        responses.forEach(response -> {
            switch(response.getType()){
                case REST:
                    log.info("Skipping rest response");
                    break;
                case EVENT_HUB:
                    log.info("Sending to Event Hub: {}", response.getFqdn());
                    azPublisherService.publish(response.getType(),response.getFqdn() ,resolveResponseBody(response, context))
                            .subscribe(log::info);
                    break;
                case SERVICEBUS:
                    log.info("Sending to Service Bus: {}", response.getFqdn());
                    azPublisherService.publish(response.getType(),response.getFqdn(), resolveResponseBody(response, context))
                            .subscribe(log::info);
                    break;
                case KAFKA:
                    log.info("Response is Kafka");
                    break;
                default:
                    log.info("Response is unknown");
            }
        });

    }

    @Override
    public void modifyRequest(String subsystem, List<Request> request) {
        SubSystem subSystem = subsystemRepository.findById(subsystem).orElseThrow();
        request.forEach(r ->
        {
            r.setSubSystem(subSystem);
            requestRepository.findById(r.getId()).ifPresentOrElse(
                    req -> {
                        req.setBody(r.getBody());
                        req.setHeaders(r.getHeaders());
                        requestRepository.save(req);
                    },
                    () -> {
                        requestRepository.save(r);
                        pathCache.insert(subsystem, r.getId().getPath());
                    }
            );
        });
    }
    //Added so that method name can be passed case insensitive
    @Override
    public RequestId getRequestId(String subSystemID, String method, String path) {
        return new RequestId(path, HttpMethod.valueOf(method.toUpperCase()), subSystemID);
    }

    @Override
    public RequestId getRequestId(String subSystemID, RequestId partialRequestId) {
        return new RequestId(partialRequestId.getPath(), partialRequestId.getMethod(), subSystemID);
    }
}
