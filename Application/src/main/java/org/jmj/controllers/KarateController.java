package org.jmj.controllers;
import com.intuit.karate.junit5.Karate;
import org.jmj.services.karate.Runner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/karate")
public class KarateController {

    Runner runner;
    KarateController(Runner runner) {
        this.runner = runner;
    }

    @GetMapping("/runTest")
    public ResponseEntity<String> runTest() throws IOException {
        String classpathStr = System.getProperty("java.class.path");
//        System.out.print(classpathStr);
        return runner.testSingleFeature("classpath:./features/request.feature");
    }
}