package org.jmj.controllers;

import org.jmj.services.karate.KarateRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/karate")
public class KarateController {

    KarateRunner karateRunner;
    KarateController(KarateRunner karateRunner) {
        this.karateRunner = karateRunner;
    }

    @GetMapping("/runTest")
    public ResponseEntity<String> runTest() throws IOException {
        String classpathStr = System.getProperty("java.class.path");
//        System.out.print(classpathStr);
        return karateRunner.testSingleFeature();
    }
}