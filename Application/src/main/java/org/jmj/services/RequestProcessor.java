package org.jmj.services;

import org.jmj.entity.Request;
import org.jmj.entity.RequestId;
import org.jmj.entity.SubSystem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RequestProcessor {
     public String processRequest(ServerHttpRequest req, HttpHeaders headers, SubSystem subSystem);
     public void modifyRequest(String subsystem, List<Request> request);
     public RequestId getRequestId(String subSystemID, String method, String path);
     public String updateStatusForRequest(RequestId requestId, HttpStatus status);

//    public default String processRequestStr(ServerHttpRequest req, HttpHeaders headers, SubSystem subSystem){return null;} ;

}
