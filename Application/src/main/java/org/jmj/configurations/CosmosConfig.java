package org.jmj.configurations;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//Todo: Cosmos need to fix issue of flux
//Todo: Make connection dynamic through properties
public class CosmosConfig {

    @Value("${spring.cloud.azure.cosmos.endpoint}")
    private String cosmosEndpoint;

    @Value("${spring.cloud.azure.cosmos.database}")
    private String cosmosDatabaseName;

    @Value("${spring.cloud.azure.cosmos.client.connection-mode}")
    private String connectionMode;

    @Value("${spring.cloud.azure.cosmos.key}")
    private String key;

    @Bean
    public CosmosClient cosmosClient() {
        return new CosmosClientBuilder()
                .endpoint(cosmosEndpoint)
                .key(key)
//                .endpoint(cosmosEndpoint)
//                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

    @Bean
    public CosmosDatabase cosmosDatabase(CosmosClient cosmosClient) {
        return cosmosClient.getDatabase(cosmosDatabaseName);
    }

}