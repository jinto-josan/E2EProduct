package org.jmj.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jmj.configurations.SubSystemRegisterer;
import org.jmj.converters.HttpMethodEditor;
import org.jmj.entity.*;
import org.jmj.repository.RequestRepository;
import org.jmj.repository.ResponseRepository;
import org.jmj.repository.SubsystemRepository;
import org.jmj.services.RequestProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController("/configuration")
@Slf4j
@Data
public class ConfigurationController {
    private final SubsystemRepository subsystemRepository;
    private final RequestRepository requestRepository;
    private final ResponseRepository responseRepository;
    private final SubSystemRegisterer subSystemRegisterer;
    private final RequestProcessor requestProcessor;


    @PostMapping("/create-subsystem")
    public String createSubSystem(@RequestBody SubSystem subSystem) {
        subsystemRepository.save(subSystem);
        subSystemRegisterer.registerRestSubSystem(subSystem);
        return "Created SubSystem";
    }

    @PostMapping("/create-request/{subSystemId}")
    public String createRequest(@PathVariable("subSystemId") String subsystem,
                                @RequestBody List<Request> request) {
        requestProcessor.modifyRequest(subsystem,request);
        return "Modified Request";
    }
    @PostMapping("/update-status-request/{subSystemId}")
    public String updateStatusForRequestId(@PathVariable("subSystemId") String subSystemID,
                                           @RequestBody List<Response> responses) {
        responses.forEach(response -> {
            requestProcessor.updateStatusForRequest(requestProcessor.getRequestId(
                    subSystemID, response.getRequest().getId()), response.getStatusCode());
        });
        return "Updated Status";
    }

    @PostMapping("/create-response/{subSystemId}")
    public Map<RequestId,String> createResponse(@PathVariable("subSystemId") String subSystemID,
                                                               @RequestBody Request request) {
        return validateAndUpdateResponse(subSystemID,request);
    }

    @GetMapping("/get-response/{subSystemId}")
    public Map<Object,List<String>> getResponsesForRequestId(@PathVariable("subSystemId") String subSystemID,
                                                             @RequestParam(value = "status",required = false) Integer status,
                                                             @RequestParam("requestMethod")String method,
                                                             @RequestParam("requestPath") String path) {
        return status!=null?
                responseRepository.findByRequest_IdAndStatusCodeOrderByType(requestProcessor.getRequestId(subSystemID, method, path), HttpStatus.valueOf(status)).stream()
                        .collect(Collectors.toMap(Response::getId, response -> List.of(response.getBody()))):
                responseRepository.findByRequest_Id(requestProcessor.getRequestId(subSystemID,method,path)).stream()
                        .collect(Collectors.toMap(Response::getId, response -> List.of(response.getBody())));
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(HttpMethod.class,new HttpMethodEditor());
    }

    private Map<RequestId,String> validateAndUpdateResponse(String subSystemID, Request request){
        //only one rest response is allowed and for other - fqdn one response is allowed
        Map<RequestId,String> requestStatuses = new HashMap<>();
        RequestId requestId=requestProcessor.getRequestId(subSystemID, request.getId());
        requestRepository.findById(requestId).ifPresentOrElse(
                req -> {
                    request.getResponses().forEach(
                            resp -> {
                                try {
                                    switch (resp.getType()) {
                                        case REST:
                                            // Update if response already has a rest type for the request ID
                                            responseRepository.findByRequest_IdAndType(requestId, resp.getType()).ifPresentOrElse(existingResp -> {
                                                existingResp.setBody(resp.getBody());
                                                existingResp.setCustomProperties(resp.getCustomProperties());
                                                responseRepository.save(existingResp);
                                                requestStatuses.put(requestId, HttpStatus.OK.toString());
                                            }, () -> {
                                                resp.setRequest(req);
                                                responseRepository.save(resp);
                                                requestStatuses.put(requestId, HttpStatus.OK.toString());
                                            });
                                            break;
                                        case COSMOS:
                                            //Only one query for a container supported now
                                            responseRepository.findByRequest_IdAndFqdn(requestId, resp.getFqdn())
                                                    .ifPresentOrElse(existingResp -> {
                                                        existingResp.setBody(resp.getBody());
                                                        responseRepository.save(existingResp);
                                                        requestStatuses.put(requestId, HttpStatus.OK.toString());
                                                    }, () -> {
                                                        resp.setRequest(req);
                                                        responseRepository.save(resp);
                                                        requestStatuses.put(requestId, HttpStatus.OK.toString());
                                                    });
                                            // Update if type is cosmos based on FQDN and request ID and body which is query
//                                            responseRepository.findByRequest_idAndFqdnAndBody(requestId, resp.getFqdn(),resp.getBody())
//                                                .ifPresentOrElse(existingResp -> {
//                                                    existingResp.setBody(resp.getBody());
//                                                    responseRepository.save(existingResp);
//                                                    requestStatuses.put(requestId, HttpStatus.OK.toString());
//                                                }, () -> {
//                                                    resp.setRequest(req);
//                                                    responseRepository.save(resp);
//                                                    requestStatuses.put(requestId, HttpStatus.OK.toString());
//                                                });
                                            break;
                                        default:
                                            // Update if type is others based on FQDN and request ID
                                            responseRepository.findByRequest_IdAndFqdn(requestId, resp.getFqdn())
                                                .ifPresentOrElse(existingResp -> {
                                                    existingResp.setBody(resp.getBody());
                                                    existingResp.setCustomProperties(resp.getCustomProperties());
                                                    responseRepository.save(existingResp);
                                                    requestStatuses.put(requestId, HttpStatus.OK.toString());
                                                }, () -> {
                                                    resp.setRequest(req);
                                                    responseRepository.save(resp);
                                                    requestStatuses.put(requestId, HttpStatus.OK.toString());
                                                });
                                    }
                                } catch (TransactionSystemException e) {
                                    Throwable t = e.getCause();
                                    while ((t != null) && !(t instanceof ConstraintViolationException)) {
                                        t = t.getCause();
                                    }
                                    if (t instanceof ConstraintViolationException) {
                                        requestStatuses.put(requestId, ((ConstraintViolationException) t).getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    requestStatuses.put(requestId, e.getMessage());
                                }
                            }
                    );
                },
                () -> {
                    requestStatuses.put(requestId, HttpStatus.NOT_FOUND.toString());
                }
        );
        return requestStatuses;

    }

}
