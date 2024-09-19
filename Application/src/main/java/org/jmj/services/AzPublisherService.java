package org.jmj.services;

import lombok.extern.slf4j.Slf4j;
import org.jmj.entity.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class AzPublisherService {
    WebClient ehWebClient;
    WebClient sbWebClient;
    @Autowired
    public AzPublisherService(@Qualifier("ehWebClient") WebClient ehWebClient,
                              @Qualifier("sbWebClient") WebClient sbWebClient) {
        this.ehWebClient = ehWebClient;
        this.sbWebClient = sbWebClient;
    }
    private WebClient getWebClientForType(ResponseType type) {
        switch (type) {
            case EVENT_HUB:
                return ehWebClient;
            case SERVICEBUS:
                return sbWebClient;
            default:
                return null;
        }
    }
    private Map<String, String> parseHeaders(String headers) {
        Map<String, String> headersMap = new HashMap<>();
        String[] pairs = headers.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                headersMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return headersMap;
    }

    public Mono<String> publishEvent(WebClient webclient, String namespace, String name,
                                     Object event, Map<String,String> customProperties) {
        return webclient.post()
                .uri("https://" + namespace + ".servicebus.windows.net/" + name + "/messages")
                .bodyValue(event)
                //Adding custom properties to headers
                .headers(httpHeaders -> {
                    customProperties.forEach(httpHeaders::add);
                })
                .exchangeToMono(resp -> {
            if (resp.statusCode().is2xxSuccessful()) {
                log.info("Published to {}/{}", namespace, name);
                return resp.bodyToMono(String.class);
            } else {
                log.error("Publishing failed to {}/{}", namespace, name);
                return resp.createException().flatMap(Mono::error);
            }
        });
    }
    public Mono<String> publish(ResponseType type,String fqdn, String message,
                                Map<String,String> customProperties) {

        var eh= fqdn.split("/");
        return publishEvent(Objects.requireNonNull(getWebClientForType(type)),eh[0], eh[1],
                message, customProperties);
    }
}
