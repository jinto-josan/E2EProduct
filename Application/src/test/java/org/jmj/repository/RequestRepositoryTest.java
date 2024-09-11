//package org.jmj.repository;
//
//import org.jmj.entity.HttpMethod;
//import org.jmj.entity.Request;
//import org.jmj.entity.RequestId;
//import org.jmj.entity.SubSystem;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class RequestRepositoryTest {
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
//
//
//        Request request1 = new Request(new RequestId("/api/v1/resource", HttpMethod.GET), subSystemA);
//        Request request2 = new Request(new RequestId("/api/v1/resource", HttpMethod.POST), subSystemA);
//        requestRepository.save(request1);
//        requestRepository.save(request2);
//    }
//
//
//    @Test
//    void testFindBySubSystemName() {
//        List<Request> requests = requestRepository.findBySubSystemName("Subsystem A");
//        assertThat(requests).hasSize(2);
//    }
//}