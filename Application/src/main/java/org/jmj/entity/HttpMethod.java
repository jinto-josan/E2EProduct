package org.jmj.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS,
    TRACE;

    @JsonCreator
    public static HttpMethod fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
