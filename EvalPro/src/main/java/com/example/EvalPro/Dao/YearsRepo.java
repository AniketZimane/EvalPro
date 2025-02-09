package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.Years;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface YearsRepo extends JpaRepository<Years,Long> {
}
