package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.UniversityPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PatternRepo extends JpaRepository<UniversityPattern,Long> {
}
