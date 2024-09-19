package org.jmj.services;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AzCosmosService {

    private final CosmosDatabase cosmosDatabase;

    @Autowired
    public AzCosmosService(CosmosDatabase cosmosDatabase) {
        this.cosmosDatabase = cosmosDatabase;
    }

    public Flux<JsonNode> fetchDocumentsByQuery(String query, String containerName) {
        CosmosContainer cosmosContainer = cosmosDatabase.getContainer(containerName);
        var queryResults = cosmosContainer.queryItems(query, new CosmosQueryRequestOptions(), JsonNode.class);
        return Flux.fromIterable(queryResults.iterableByPage())
                .flatMap(feedResponse -> Flux.fromIterable(feedResponse.getResults()));
//        return Mono.justOrEmpty(queryResults.stream().findFirst().orElse(null));
//        return queryResults.stream().findFirst().orElse(null);

//        return queryResults.byPage()
//                .flatMap(feedResponse -> Flux.fromIterable(feedResponse.getResults()));
    }
}