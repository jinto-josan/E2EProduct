package org.jmj.services.caching;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record RequestPathAndContext(String path, Map<String,String> context) {

}
