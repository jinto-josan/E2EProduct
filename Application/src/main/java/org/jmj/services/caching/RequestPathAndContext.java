package org.jmj.services.caching;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record RequestPathAndContext(String path, Map<String,String> context, Map<org.jmj.entity.HttpMethod,HttpStatus> statuses) {

    //Configured so that if statuses are not configured it will sent default ok status
    //It will help in case of just configuring path and no response
    public HttpStatus getStatus(org.jmj.entity.HttpMethod method) {
        return statuses().computeIfAbsent(method, k -> HttpStatus.OK);
    }
    public HttpStatus getStatus(org.springframework.http.HttpMethod method) {
        return getStatus(org.jmj.entity.HttpMethod.valueOf(method.name()));
    }

}
