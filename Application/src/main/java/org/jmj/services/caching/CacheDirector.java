package org.jmj.services.caching;

public interface CacheDirector {
    void initializeCache();
    void refreshCache();
    void disposeCache();
}
