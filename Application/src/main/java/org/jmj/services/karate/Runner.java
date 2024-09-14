package org.jmj.services.karate;

import com.intuit.karate.Results;
import com.intuit.karate.junit5.Karate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class Runner {

    private final ResourceLoader resourceLoader;

    Runner(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ResponseEntity<String> testSingleFeature(String path) throws IOException {

        if(!resourceLoader.getResource(path).getFile().exists())
        {
            return ResponseEntity.badRequest().body("Feature file not found.");
        }
        log.info("Running test from path: {}", path);
        Results results = Karate.run(path).parallel(1);
        log.info("Test results: {}", results);
        if (results.getFailCount() == 0) {
            return ResponseEntity.ok("All tests passed.");
        } else {
            return ResponseEntity.ok().body("Some tests failed.");
        }

    }
}