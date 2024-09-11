package org.jmj.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AzEHPublisherService {
    WebClient webClient;
    @Autowired
    public AzEHPublisherService(WebClient webClient) {
        this.webClient=webClient;
    }
    public Mono<String> publishEvent(String ehNamespace, String ehName, Object event) {
         return webClient.post()
                .uri("https://" + ehNamespace + ".servicebus.windows.net/" + ehName + "/messages")
                .bodyValue(event)
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        log.info("Event published to {}/{}", ehNamespace, ehName);
                        return resp.bodyToMono(String.class);

                    } else {
                        log.error("Publishing failed to {}/{}", ehNamespace, ehName);
                       return resp.createException().flatMap(Mono::error);
                    }
                });
    }
    public Mono<String> publishEvent(String fqdn, Object event) {
        var eh= fqdn.split("/");
        return publishEvent(eh[0], eh[1], event);
    }
}
