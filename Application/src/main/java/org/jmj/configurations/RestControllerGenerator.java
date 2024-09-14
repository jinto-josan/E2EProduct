package org.jmj.configurations;


import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.jmj.entity.*;
import org.jmj.services.RequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.lang.reflect.Modifier;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Component
@Slf4j
public class RestControllerGenerator {
    private ApplicationContext applicationContext;
    private final RequestProcessor requestProcessor;
    @Autowired
    public RestControllerGenerator(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    @SneakyThrows
    public Object generateController(SubSystem subSystem) {

        return new ByteBuddy()
                .subclass(Object.class)
                //class declaration
                .name("org.jmj.controller." + subSystem.getName() + "Controller")
                .annotateType(AnnotationDescription.Builder
                        .ofType(RestController.class)
                        .build())
                // It doesnt register using RequestMapping
                .annotateType(AnnotationDescription.Builder
                        .ofType(ResponseBody.class)
                        .build())
                //post method
                .defineMethod("handleRequest", String.class, Modifier.PUBLIC)
                .withParameter(ServerHttpRequest.class)
                .withParameter(HttpHeaders.class)
                .annotateParameter(AnnotationDescription.Builder.ofType(RequestHeader.class).build())
                .intercept(MethodDelegation.to(new SubSystemControllerMethodsImplementation(subSystem)))

                //generating instance which can be injected
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();


    }


    public class SubSystemControllerMethodsImplementation{


        private final SubSystem subSystem;
        SubSystemControllerMethodsImplementation(SubSystem subSystem) {
            this.subSystem = subSystem;
        }


        public String handleRequest(@Argument(0) ServerHttpRequest req, @Argument(1) HttpHeaders headers) {
            var response= requestProcessor.processRequest(req, headers, subSystem);
            return switch ((HttpStatus) response.getStatusCode()) {
                case OK -> response.getBody();
                default -> throw new ResponseStatusException(response.getStatusCode(), response.getBody());
            };
        }


    }



}
