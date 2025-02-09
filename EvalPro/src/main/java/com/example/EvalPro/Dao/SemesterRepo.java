package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface SemesterRepo  extends JpaRepository<Semester,Long> {
    @Query(value = "SELECT * FROM semester WHERE stream_id = :streamId", nativeQuery = true)
    List<Semester> findByStreamId(@Param("streamId") Long streamId);

}
