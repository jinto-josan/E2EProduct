package org.jmj.configurations;


import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredentialBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Objects;


@Component
public class AzWebClient {
    private final TokenCredential credential;
    public AzWebClient(){
        //so that it uses SPN, WorkloadIdentity, MI or IDE
        this.credential = new DefaultAzureCredentialBuilder().build();
    }
    //Two clients because azure doesnt allow same token for two different resources
    @Bean
    @Qualifier("ehWebClient")
    public WebClient webClientEh() {
        var request=new TokenRequestContext();
        request.setScopes(Arrays.asList("https://eventhubs.azure.net/.default"));
        return WebClient.builder()
                .defaultHeader("Authorization", "Bearer " + Objects.requireNonNull(credential.getToken(request).block()).getToken())
                .build();
    }
    @Bean
    @Qualifier("sbWebClient")
    public WebClient webClientSb() {
        var request=new TokenRequestContext();
        request.setScopes(Arrays.asList("https://servicebus.azure.net/.default"));
        return WebClient.builder()
                .defaultHeader("Authorization", "Bearer " + Objects.requireNonNull(credential.getToken(request).block()).getToken())
                .build();
    }
}
