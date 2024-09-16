package org.jmj.services.karate;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import lombok.extern.slf4j.Slf4j;
import org.jmj.constants.Constants;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
@Slf4j
public class KarateRunner {

    private final ResourceLoader resourceLoader;
    private final Environment env;

    KarateRunner(ResourceLoader resourceLoader, Environment env) throws IOException {
        this.resourceLoader = resourceLoader;
        this.env = env;
        setKarateProperties();

    }

    public ResponseEntity<String> testSingleFeature() throws IOException {
        var path=Constants.KARATE_BASE_DIR;
        /*
            working dir - default is absolute path of cwd. It is used to get relative path.
            config dir - default is working-dir. It is used to get karate-config.js
            path[] - directory where feature files are located or .feature file.
            relativeTo - used to get feature files relative to a class. It helps in generating path
            relative to this relative
            All above are absolute and are not relative to anything.
            Paths in js or feature file are relative to .feature file if this: used else use classpath:
         */
        log.info("Running test from path: {}", path);
        Results results = Runner.path(path)
//                .workingDir(new File(Constants.KARATE_BASE_DIR))
                .configDir(Constants.KARATE_BASE_DIR+"config/")
                .parallel(1);
        log.info("Test results: {}", results);

        if (results.getFailCount() == 0) {
            return ResponseEntity.ok("All tests passed.");
        } else {
            return ResponseEntity.ok().body("Some tests failed.");
        }

    }



    //For setting global properties which are static.


    private void setKarateProperties() throws IOException {
        Properties properties = new Properties();

        // Load properties from karate.properties if it exists
        Resource propertiesFile = resourceLoader.getResource(Constants.KARATE_BASE_DIR+"karate.properties");
        if (propertiesFile.exists()) {
            properties.load(propertiesFile.getInputStream());
        }


        // Override properties with environment variables if they exist
        properties.forEach((key, value) -> {
            String envValue = env.getProperty((String) key);
            if (envValue != null) {
                System.setProperty((String) key, envValue);
            } else {
                System.setProperty((String) key, (String) value);
            }
        });
    }
}