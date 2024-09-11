package org.jmj.configurations;


import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.identity.IntelliJCredentialBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Objects;


@Component
public class AzWebClient {
    private final TokenCredential credential;
    public AzWebClient(){
        //so that it uses SPN, WorkloadIdentity, MI or IDE
        this.credential = new DefaultAzureCredentialBuilder().build();
    }
    @Bean
    public WebClient webClient() {
        var request=new TokenRequestContext();
        request.setScopes(Collections.singletonList("https://eventhubs.azure.net/.default"));
        return WebClient.builder()
                .defaultHeader("Authorization", "Bearer " + Objects.requireNonNull(credential.getToken(request).block()).getToken())
                .build();
    }
}
