//package org.jmj.repository;
//
//import lombok.extern.slf4j.Slf4j;
//import org.jmj.entity.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.http.HttpStatus;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@Slf4j
//public class ResponseRepositoryTest {
//
//    @Autowired
//    private ResponseRepository responseRepository;
//
//    @Autowired
//    private RequestRepository requestRepository;
//
//    @Autowired
//    private SubsystemRepository subsystemRepository;
//
//    @BeforeEach
//    void setUp() {
//        SubSystem subSystemA = new SubSystem("Subsystem A");
//        subsystemRepository.save(subSystemA);
//        RequestId requestId= new RequestId("/api/v1/resource", HttpMethod.GET);
//
//        Request request1 = new Request(requestId, subSystemA);
//        requestRepository.save(request1);
//
//        Response response1 = new Response(new ResponseId(requestId, HttpStatus.OK));
//        responseRepository.save(response1);
//    }
//
//    @Test
//    void testFindByRequestIdAndStatusCode() {
//        ResponseId responseId = new ResponseId(new RequestId("/api/v1/resource", HttpMethod.GET), HttpStatus.OK);
//        Optional<Response> response = responseRepository.findById(responseId);
//        assertThat(response).isPresent();
//    }
//
//    @Test
//    void testFindByRequestId() {
//        RequestId requestID = new RequestId("/api/v1/resource", HttpMethod.GET);
//        List<Response> response = responseRepository.findById_RequestId(requestID);
//        assertThat(response).hasSize(1);;
//    }
//}