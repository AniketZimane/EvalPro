package com.example.EvalPro.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Years {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "year_seq")
    @SequenceGenerator(name = "year_seq")
    Long id;
    String year;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Years() {
    }

    public Years(Long id, String year, LocalDateTime createdDate) {
        this.id = id;
        this.year = year;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Years{" +
                "id=" + id +
                ", year='" + year + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
