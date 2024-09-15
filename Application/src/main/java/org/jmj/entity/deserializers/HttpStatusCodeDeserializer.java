package org.jmj.entity.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class HttpStatusCodeDeserializer
        extends JsonDeserializer<HttpStatus> {
    @Override
    public HttpStatus deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        // read data as JsonNode and here node is that specific field been sent
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        // get status value as text and store in a variable
        return HttpStatus.valueOf(node.asInt());
    }

}
