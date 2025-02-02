package com.example.EvalPro.Dao;

import com.example.EvalPro.Entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RegistrationRepo extends JpaRepository<Registration,Long> {
}
