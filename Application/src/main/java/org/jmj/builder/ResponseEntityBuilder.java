package org.jmj.builder;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.jmj.entity.Request;
import org.jmj.entity.Response;
import org.jmj.entity.ResponseId;
import org.jmj.entity.ResponseType;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Slf4j
public class ResponseEntityBuilder {

    private Optional<Response> resp;


    public ResponseEntityBuilder responseId(Request req, int status, String type){
        resp= Optional.of(new Response());
        resp.get().setId(new ResponseId(req.getId(), HttpStatus.valueOf(status),ResponseType.valueOf(type)));
        return this;
    }
    public ResponseEntityBuilder order(int order){
        resp.get().setResponseOrder(order);
        return this;
    }
    public ResponseEntityBuilder body(JsonNode body){
        String bodyStr = body.toString();
        resp.get().setBody(bodyStr);
        return this;
    }
    public ResponseEntityBuilder fqdn(String fqdn){
        if(fqdn!=null){
            resp.get().setFqdn(fqdn);
        }
        log.info(resp.get().getFqdn());
        return this;
    }
    public Optional<Response> build(){
        return resp;
    }
}
