package org.jmj.services.caching;

import org.jmj.entity.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Map;

public interface PathCache {
    void insert(String subSystemName, String path);
    RequestPathAndContext search(String subSystemName, String path);
    void updateStatus(String subSystemName, String path, HttpMethod method, HttpStatus status) ;
    void update(String subSystemName, String oldPath, String newPath);
    void delete(String subSystemName, String path);
    void clear();
    //Its applicable only at the top level root
    void clear(String root);
}
