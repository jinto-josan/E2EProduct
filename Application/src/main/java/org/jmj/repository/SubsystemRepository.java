package org.jmj.repository;

import org.jmj.entity.SubSystem;
import org.jmj.repository.projections.SubsystemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubsystemRepository extends JpaRepository<SubSystem, String> {

    //Get all the subsystems names
    List<SubsystemProjection> findAllBy();
}
