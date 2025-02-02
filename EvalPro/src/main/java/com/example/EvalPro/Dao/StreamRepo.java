package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface StreamRepo extends JpaRepository<Stream,Long> {
}
