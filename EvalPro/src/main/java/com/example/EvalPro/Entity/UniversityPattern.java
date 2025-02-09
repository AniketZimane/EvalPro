package com.example.EvalPro.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
public class UniversityPattern {
    @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pattern_seq")
            @SequenceGenerator(name="pattern_seq")
    Long id;
    String name;
    @CreationTimestamp
    @Column(updatable = false)
    LocalDate createdDate;

    public UniversityPattern(Long id, String name, LocalDate createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
    }

    public UniversityPattern() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Pattern{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }


}
