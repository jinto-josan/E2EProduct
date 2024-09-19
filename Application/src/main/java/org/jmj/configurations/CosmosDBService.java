//package org.jmj.configurations;
//
//
//    import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
//import java.time.Instant;
//import java.util.Base64;
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//    public class CosmosDBService {
//
//        private static final String COSMOS_DB_ACCOUNT = "your-cosmos-db-account";
//        private static final String COSMOS_DB_KEY = "your-cosmos-db-key";
//        private static final String COSMOS_DB_DATABASE = "your-database";
//        private static final String COSMOS_DB_CONTAINER = "your-container";
//        private static final String COSMOS_DB_ENDPOINT = "https://" + COSMOS_DB_ACCOUNT + ".documents.azure.com:443/";
//
//        public static void main(String[] args) throws Exception {
//            String query = "SELECT * FROM c";
//            JsonNode result = queryCosmosDB(query);
//            System.out.println(result.toPrettyString());
//        }
//
//        private static JsonNode queryCosmosDB(String query) throws Exception {
//            String resourceLink = "dbs/" + COSMOS_DB_DATABASE + "/colls/" + COSMOS_DB_CONTAINER;
//            String authToken = generateAuthToken("POST", resourceLink, "docs");
//
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI(COSMOS_DB_ENDPOINT + resourceLink + "/docs"))
//                    .header("Authorization", authToken)
//                    .header("x-ms-date", Instant.now().toString())
//                    .header("x-ms-version", "2018-12-31")
//                    .header("Content-Type", "application/query+json")
//                    .POST(HttpRequest.BodyPublishers.ofString("{\"query\":\"" + query + "\"}"))
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readTree(response.body());
//        }
//
//        private static String generateAuthToken(String verb, String resourceLink, String resourceType) throws Exception {
//            String key = COSMOS_DB_KEY;
//            String date = Instant.now().toString();
//            String stringToSign = verb.toLowerCase() + "\n" +
//                    resourceType.toLowerCase() + "\n" +
//                    resourceLink + "\n" +
//                    date.toLowerCase() + "\n" +
//                    "" + "\n";
//
//            Mac mac = Mac.getInstance("HMACSHA256");
//            mac.init(new SecretKeySpec(Base64.getDecoder().decode(key), "HMACSHA256"));
//            String signature = Base64.getEncoder().encodeToString(mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8)));
//
//            return URLEncoder.encode("type=master&ver=1.0&sig=" + signature, StandardCharsets.UTF_8);
//        }
//    }
//
