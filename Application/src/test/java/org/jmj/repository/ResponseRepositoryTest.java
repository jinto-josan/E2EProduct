package org.jmj.repository;

import org.jmj.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ResponseRepositoryTest {

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private SubsystemRepository subsystemRepository;

    @Test
    public void testSaveAndFindByRequestId() {
        SubSystem subSystem = new SubSystem();
        subSystem.setName("TestSubSystem");
        subsystemRepository.save(subSystem);

        RequestId requestId = new RequestId("/test", HttpMethod.GET, subSystem.getName());
        Request request = new Request();
        request.setId(requestId);
        requestRepository.save(request);

        Response response = new Response();
        response.setRequest(request);
        response.setBody("{\"message\":\"test\"}");
        responseRepository.save(response);

        List<Response> found = responseRepository.findByRequest_Id(requestId);
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getBody()).isEqualTo("{\"message\":\"test\"}");
    }
}