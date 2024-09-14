package org.jmj.services;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jmj.builder.ResponseEntityBuilder;
import org.jmj.entity.*;
import org.jmj.repository.RequestRepository;
import org.jmj.repository.ResponseRepository;
import org.jmj.repository.SubsystemRepository;
import org.jmj.services.caching.PathCache;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jmj.entity.ResponseType.REST;

@Slf4j
@Service
@Data
public class RequestProcessorImpl implements RequestProcessor {
    private final ResponseRepository responseRepository;
    private final SubsystemRepository subsystemRepository;
    private final RequestRepository requestRepository;
    private final AzEHPublisherService azEHPublisherService;
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
        List<Response> responses = responseRepository.findById_RequestIdAndId_StatusCodeOrderById_Type(
                getRequestId(subSystem.getName(), req.getMethod().name(), actRqstPthAndCtxt.path()),
                HttpStatus.OK);
        if (responses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No response configuration found for the request");
        }

        processAsyncResponses(responses, actRqstPthAndCtxt.context());

        return responses.stream()
                .filter(response -> response.getId().getType() == REST)
                .findFirst()
                .map(response -> {
                    log.info("Response found: {}", response.getBody());
                    return resolveResponseBody(response, actRqstPthAndCtxt.context());
                })
                //If no rest response it means some async request was present
                .orElseGet(() -> ResponseEntity.accepted().body("Request Accepted for processing"));

    }

    private ResponseEntity<String> resolveResponseBody(Response response, Map<String, String> context){
        String responseBody = response.getBody();
        for (Map.Entry<String, String> entry : context.entrySet()) {
            responseBody = responseBody.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return ResponseEntity.ok().body(responseBody);
    }



    @Async
    private void processAsyncResponses(List<Response> responses, Map<String, String> context){
        //response processed in order of response order
        responses.forEach(response -> {
            switch(response.getId().getType()){
                case REST:
                    log.info("Skipping rest response");
                    break;
                case EVENT_HUB:
                    log.info("Sending to Event Hub: {}", response.getFqdn());
                    azEHPublisherService.publishEvent(response.getFqdn() ,resolveResponseBody(response, context))
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
}
