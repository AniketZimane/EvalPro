package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ResultRepo extends JpaRepository<Result, Long> {

    @Query("SELECT r FROM Result r WHERE r.studentId = :studentId AND r.motherName = :motherName")
    List<Result> findByStudentIdAndMotherName(@Param("studentId") String studentId, @Param("motherName") String motherName);

    @Query("SELECT r FROM Result r WHERE r.studentId = :studentId")
    List<Result> findByStudentId(@Param("studentId") String studentId);
}

