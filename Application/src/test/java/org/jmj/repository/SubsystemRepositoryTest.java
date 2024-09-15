package org.jmj.repository;

import org.jmj.entity.SubSystem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SubsystemRepositoryTest {

    @Autowired
    private SubsystemRepository subsystemRepository;

    @Test
    public void testSaveAndFindById() {
        SubSystem subSystem = new SubSystem();
        subSystem.setName("TestSubSystem");
        subsystemRepository.save(subSystem);

        Optional<SubSystem> found = subsystemRepository.findById(subSystem.getName());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("TestSubSystem");
    }
}