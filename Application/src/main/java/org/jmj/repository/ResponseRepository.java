package org.jmj.repository;

import org.jmj.entity.RequestId;
import org.jmj.entity.Response;
import org.jmj.entity.ResponseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<Response, ResponseId> {
//    @Query("SELECT r FROM Response r WHERE r.request.id = :requestId AND r.id.statusCode = :statusCode")
//    List<Response> findByRequest_IdAndId_StatusCode(RequestId requestId, HttpStatus statusCode);
//    @Query("SELECT r FROM Response r WHERE r.request_path=:requestId and r.request_method = :requestId AND r.id.statusCode = :statusCode")
//    List<Response> findById(RequestId requestId);

    List<Response> findById_RequestId(RequestId requestId);
    List<Response> findById_RequestIdOrderById_Type(RequestId requestId);
//    List<Response> findById_RequestIdOrderByResponseOrderAndId_Type(RequestId requestId);
    List<Response> findById_RequestIdAndId_StatusCodeOrderById_Type(RequestId requestId, HttpStatus statusCode);
}
