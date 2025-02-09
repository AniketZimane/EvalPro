package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.RevaluationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RevaluationRequestRepo extends JpaRepository<RevaluationRequest,Long> {
}
