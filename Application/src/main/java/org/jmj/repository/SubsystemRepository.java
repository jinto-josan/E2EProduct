package org.jmj.repository;

import org.jmj.entity.SubSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsystemRepository extends JpaRepository<SubSystem, String> {
}
