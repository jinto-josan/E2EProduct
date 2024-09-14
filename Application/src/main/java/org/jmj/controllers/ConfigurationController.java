package org.jmj.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jmj.builder.ResponseEntityBuilder;
import org.jmj.configurations.SubSystemRegisterer;
import org.jmj.converters.HttpMethodEditor;
import org.jmj.entity.*;
import org.jmj.repository.RequestRepository;
import org.jmj.repository.ResponseRepository;
import org.jmj.repository.SubsystemRepository;
import org.jmj.services.RequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    public String createRequest(@PathVariable("subSystemId") String subsystem, @RequestBody List<Request> request) {
        requestProcessor.modifyRequest(subsystem,request);
        return "Modified Request";
    }
//    @PostMapping("/update-status-request/{subSystemId}")
//    public String updateStatusForRequestId(@PathVariable("subSystemId") String subSystemID,
//                                           @RequestParam("requestMethod")String method,
//                                           @RequestParam("requestPath") String path, @RequestParam("status") Integer status) {
//        requestProcessor.updateStatusForRequest(requestProcessor.getRequestId(subSystemID,method,path), HttpStatus.valueOf(status));
//        return "Updated Status";
//    }

    @PostMapping("/create-response/{subSystemId}")
    public Map<RequestId,String> createResponse(@PathVariable("subSystemId") String subSystemID, @RequestParam("requestMethod")String method, @RequestParam("requestPath") String path, @RequestBody List<JsonNode> responses) {
//Todo: Process of multiple response and their status to be returned
        Map<RequestId,String> requestStatuses = new HashMap<>();
        responses.forEach(
                    resp -> {
                        requestRepository.findById(requestProcessor.getRequestId(subSystemID, method, path)).ifPresentOrElse(
                                req -> {
                                    try {
                                        new ResponseEntityBuilder()
                                                .responseId(req, resp.get("status").asInt(), resp.get("type").asText())
                                                .body(resp.get("body"))
                                                .order(resp.get("responseOrder").asInt())
                                                .fqdn(resp.has("fqdn")?resp.get("fqdn").asText():null)
                                                .build()
                                                .ifPresent(responseRepository::save);
                                        requestStatuses.put(requestProcessor.getRequestId(subSystemID, method, path), HttpStatus.CREATED.toString());
                                    }
                                    catch (TransactionSystemException e){
                                        Throwable t = e.getCause();
                                        while ((t != null) && !(t instanceof ConstraintViolationException)) {
                                            t = t.getCause();
                                        }
                                        if (t instanceof ConstraintViolationException) {
                                            requestStatuses.put(requestProcessor.getRequestId(subSystemID, method, path),((ConstraintViolationException) t).getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        requestStatuses.put(requestProcessor.getRequestId(subSystemID, method, path),e.getMessage());
                                    }

                                },
                                () -> {
                                    requestStatuses.put(requestProcessor.getRequestId(subSystemID, method, path),HttpStatus.NOT_FOUND.toString());
                                }
                        );
                }
        );
        return requestStatuses;
    }

    @GetMapping("/get-response/{subSystemId}")
    public Map<Object,List<String>> getResponsesForRequestId(@PathVariable("subSystemId") String subSystemID,
                                                             @RequestParam(value = "status",required = false) Integer status,
                                                             @RequestParam("requestMethod")String method,
                                                             @RequestParam("requestPath") String path) {
        return status!=null?
                responseRepository.findById_RequestIdAndId_StatusCodeOrderById_Type(requestProcessor.getRequestId(subSystemID, method, path), HttpStatus.valueOf(status)).stream()
                        .collect(Collectors.toMap(Response::getId, response -> List.of(response.getBody()))):
                responseRepository.findById_RequestId(requestProcessor.getRequestId(subSystemID,method,path)).stream()
                        .collect(Collectors.toMap(Response::getId, response -> List.of(response.getBody())));
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(HttpMethod.class,new HttpMethodEditor());
    }

}
