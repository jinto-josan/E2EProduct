package org.jmj.repository;

import org.jmj.entity.Request;
import org.jmj.entity.RequestId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, RequestId> {

    //Todo: Need to use projection to get only the path and method
    @Query("SELECT r.id FROM Request r WHERE r.subSystem.name = :subSystemName")
//    @Query(value = "SELECT r.method, r.path FROM Request r WHERE r.sub_system_id = ?1",
//            countQuery = "SELECT count(*) FROM Request r WHERE r.sub_system_id = ?1",
//            nativeQuery = true)
    Page<RequestId> findBySubSystemName(String subSystemName, Pageable pageable);
    //Name is used because not required to load requests.json in subsystem eagerly
    @Query("SELECT DISTINCT r.id.path FROM Request r WHERE r.subSystem.name = :subSystemName")
    List<String> findDistinctPathsBySubSystemName(@Param("subSystemName") String subSystemName);

}
