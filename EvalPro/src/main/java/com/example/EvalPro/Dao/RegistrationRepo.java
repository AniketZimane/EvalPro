package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RegistrationRepo extends JpaRepository<Registration,Long> {
    @Query("SELECT r.email FROM Registration r WHERE r.studentId = :studentId")
    String findEmailByStudentId(@Param("studentId") String studentId);

}
