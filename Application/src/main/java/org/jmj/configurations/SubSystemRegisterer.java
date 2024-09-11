package org.jmj.configurations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jmj.entity.SubSystem;
import org.jmj.repository.SubsystemRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;


@Slf4j
@Component
@RequiredArgsConstructor
public class SubSystemRegisterer implements InitializingBean {

    private final SubsystemRepository subsystemRepository;
    private final RestControllerGenerator subSystemControllerGenerator;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Registering Controllers");
        subsystemRepository.findAll().forEach(this::registerRestSubSystem);
    }

    public void registerRestSubSystem(SubSystem subSystem){
        log.info("Registering REST SubSystem: {}", subSystem.getName());
        var generatedController = subSystemControllerGenerator.generateController(subSystem);
        try {
            requestMappingHandlerMapping.registerMapping(
                    RequestMappingInfo.paths("/"+subSystem.getName()+"/**")
                            .build(),
                    generatedController,
                    generatedController.getClass().getMethod("handleRequest", ServerHttpRequest.class, HttpHeaders.class)

            );
        } catch (NoSuchMethodException e) {
            log.error("Method not found",e);
            throw new RuntimeException(e);
        }
    }
}
