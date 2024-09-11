package org.jmj.services.caching;

import org.springframework.http.HttpStatus;

import java.util.Map;

//Todo: Logic to implement status code for each type of method
public record RequestPathAndContext(String path, Map<String,String> context, HttpStatus status) {

}
