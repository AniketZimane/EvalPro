package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.RevaluationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface RevaluationRequestRepo extends JpaRepository<RevaluationRequest,Long> {

    List<RevaluationRequest> findRevaluationRequestsBySeatNumber(String seatNumber);

    @Query("SELECT r FROM RevaluationRequest r WHERE r.isEvaluate = 'yes' AND (r.isModerate IS NULL OR r.isModerate = '')")
    List<RevaluationRequest> findEvaluatedButNotModeratedRequests();


    @Query("SELECT r FROM RevaluationRequest r WHERE r.isEvaluate = 'no'")
    List<RevaluationRequest> findUnevaluatedRevaluationRequests();

    @Query("SELECT r FROM RevaluationRequest r WHERE r.isEvaluate = 'yes' AND r.isModerate = 'yes' AND (r.isVerify IS NULL OR r.isVerify = '' OR r.isVerify = 'no')")
    List<RevaluationRequest> findEvaluatedModeratedButNotVerifiedRequests();



}
