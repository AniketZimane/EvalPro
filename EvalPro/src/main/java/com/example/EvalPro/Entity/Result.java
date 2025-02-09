package com.example.EvalPro.Entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "result_seq")
    @SequenceGenerator(name = "result_seq")
    private Long id;

    private String studentId;
    private String motherName;
    private String pattern;
    private String stream;
    private String year;
    private String sem;
    private String publishDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String subjectMarksJson;

    // Convert Map<String, Integer> to JSON string before persisting
    public void setSubjectMarks(Map<String, Integer> subjectMarks) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.subjectMarksJson = objectMapper.writeValueAsString(subjectMarks);
        } catch (IOException e) {
            throw new RuntimeException("Error converting subject marks to JSON", e);
        }
    }

    // Convert JSON string back to Map<String, Integer> when retrieving
    public Map<String, Integer> getSubjectMarks() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(this.subjectMarksJson, new TypeReference<Map<String, Integer>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to subject marks", e);
        }
    }

    public Result(Long id, String studentId, String motherName, String pattern, String stream, String year, String sem, String publishDate, String subjectMarksJson) {
        this.id = id;
        this.studentId = studentId;
        this.motherName = motherName;
        this.pattern = pattern;
        this.stream = stream;
        this.year = year;
        this.sem = sem;
        this.publishDate = publishDate;
        this.subjectMarksJson = subjectMarksJson;
    }

    public Result() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getSubjectMarksJson() {
        return subjectMarksJson;
    }

    public void setSubjectMarksJson(String subjectMarksJson) {
        this.subjectMarksJson = subjectMarksJson;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", motherName='" + motherName + '\'' +
                ", pattern='" + pattern + '\'' +
                ", stream='" + stream + '\'' +
                ", year='" + year + '\'' +
                ", sem='" + sem + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", subjectMarksJson='" + subjectMarksJson + '\'' +
                '}';
    }
}
