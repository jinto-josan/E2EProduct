package org.jmj.repository;

import org.jmj.entity.SubSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SubsystemRepositoryTest {

    @Autowired
    private SubsystemRepository subsystemRepository;

    @BeforeEach
    void setUp() {
        SubSystem subSystemA = new SubSystem("Subsystem A");
        SubSystem subSystemB = new SubSystem("Subsystem B");
        subsystemRepository.save(subSystemA);
        subsystemRepository.save(subSystemB);
    }

    @Test
    void testFindAll() {
        List<SubSystem> subsystems = subsystemRepository.findAll();
        assertThat(subsystems).hasSize(2);
    }
}