package org.jmj.services.caching;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jmj.entity.Request;
import org.jmj.entity.SubSystem;
import org.jmj.repository.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Data
public class TrieCacheDirector implements CacheDirector, InitializingBean {
    private final SubsystemRepository subSystemRepository;
    private final RequestRepository requestRepository;
    private final PathCache pathCache ;

    @Override
    public void initializeCache() {
        log.info("Initializing Trie Cache");
        List<SubSystem> subSystems = subSystemRepository.findAll();
        subSystems.forEach(subSystem -> {
            List<String> requestPaths = requestRepository.findDistinctPathsBySubSystemName(subSystem.getName());
            requestPaths.forEach(path -> {
                pathCache.insert(subSystem.getName(), path);
            });
        });
    }

    @Override
    public void refreshCache() {
        log.info("Refreshing Trie Cache");
        disposeCache();
        initializeCache();
    }

    @Override
    public void disposeCache() {
        log.info("Disposing Trie Cache");
        pathCache.clear();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initializeCache();
    }
}