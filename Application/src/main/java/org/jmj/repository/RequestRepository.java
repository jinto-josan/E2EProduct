package org.jmj.repository;

import org.jmj.entity.Request;
import org.jmj.entity.RequestId;
import org.jmj.entity.SubSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, RequestId> {
//    @Query("SELECT r FROM Request r WHERE r.subSystem.name = :subSystemName")
    List<Request> findBySubSystemName(String subSystemName);
    //Name is used because not required to load requests in subsystem eagerly
    @Query("SELECT DISTINCT r.id.path FROM Request r WHERE r.subSystem.name = :subSystemName")
    List<String> findDistinctPathsBySubSystemName(@Param("subSystemName") String subSystemName);

}
