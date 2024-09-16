//package org.jmj.controllers;
//
//import lombok.extern.slf4j.Slf4j;
//import org.jmj.services.AzPublisherService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Mono;
//
//@RestController
//@Slf4j
//@RequestMapping("/eh/publish/{ehNamespace}/{ehName}")
//public class EHPublisher {
//    AzPublisherService azEHPublisherService;
//
//    @Autowired
//    EHPublisher(AzPublisherService azEHPublisherService) {
//        this.azEHPublisherService = azEHPublisherService;
//    }
//
//
//
//    @GetMapping("/{event}")
//    public Mono<String> publishEventTest(@PathVariable("ehNamespace") String ehNamespace,@PathVariable("ehName") String ehName, @PathVariable("event") String event) {
//        return azEHPublisherService.publishEvent(ehNamespace, ehName, event);
//    }
//    @PostMapping
//    public Mono<String> publishEventPost(@PathVariable("ehNamespace") String ehNamespace,@PathVariable("ehName") String ehName, @RequestBody Object event) {
//
//        return azEHPublisherService.publishEvent(ehNamespace, ehName, event);
//
//    }
//
//}
