package com.example.EvalPro.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Stream {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "stream_seq")
    @SequenceGenerator(name = "stream_seq")
    Long id;
    String name;
    Float yearCount;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Stream(Long id, String name, Float yearCount, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.yearCount = yearCount;
        this.createdDate = createdDate;
    }

    public Stream() {
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

    public Float getYearCount() {
        return yearCount;
    }

    public void setYearCount(Float yearCount) {
        this.yearCount = yearCount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Stream{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yearCount=" + yearCount +
                ", createdDate=" + createdDate +
                '}';
    }
}
