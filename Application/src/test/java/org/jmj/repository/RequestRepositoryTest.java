package org.jmj.repository;

import org.jmj.entity.HttpMethod;
import org.jmj.entity.Request;
import org.jmj.entity.RequestId;
import org.jmj.entity.SubSystem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private SubsystemRepository subsystemRepository;

    @Test
    public void testSaveAndFindById() {
        SubSystem subSystem = new SubSystem();
        subSystem.setName("TestSubSystem");
        subsystemRepository.save(subSystem);

        RequestId requestId = new RequestId("/test", HttpMethod.GET, subSystem.getName());
        Request request = new Request();
        request.setId(requestId);
        requestRepository.save(request);

        Optional<Request> found = requestRepository.findById(requestId);
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(requestId);
    }
}