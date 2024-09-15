package org.jmj.repository;

import org.jmj.entity.RequestId;
import org.jmj.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
//    @Query("SELECT r FROM Response r WHERE r.request.id = :requestId AND r.id.statusCode = :statusCode")
//    List<Response> findByRequest_IdAndId_StatusCode(RequestId requestId, HttpStatus statusCode);
//    @Query("SELECT r FROM Response r WHERE r.request_path=:requestId and r.request_method = :requestId AND r.id.statusCode = :statusCode")
//    List<Response> findById(RequestId requestId);

    List<Response> findByRequest_Id(RequestId requestId);
    List<Response> findByRequest_IdAndStatusCodeOrderByType(RequestId requestId, HttpStatus statusCode);
}
