package com.example.EvalPro.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "semester_seq")
    @SequenceGenerator(name = "semester_seq")
    Long id;
    String streamId;
    String name;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Semester(Long id, String streamId, String name, LocalDateTime createdDate) {
        this.id = id;
        this.streamId = streamId;
        this.name = name;
        this.createdDate = createdDate;
    }

    public Semester() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Semester{" +
                "id=" + id +
                ", streamId='" + streamId + '\'' +
                ", name='" + name + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
