package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.Semester;
import com.example.EvalPro.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface SubjectRepo extends JpaRepository<Subject,Long> {

    @Query("SELECT s.sub_name FROM Subject s WHERE s.stream_id = :stream_id AND s.sem_id = :sem_id")
    List<String> findSubjectNamesByStreamAndSemester(@Param("stream_id") Long stream_id,
                                                     @Param("sem_id") Long sem_id);}
