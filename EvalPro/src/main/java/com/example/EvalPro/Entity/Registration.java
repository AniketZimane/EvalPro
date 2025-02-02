package com.example.EvalPro.Entity;

import jakarta.persistence.*;

@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reg_seq")
    @SequenceGenerator(name = "reg_seq")
    public Long id;
    String name;
    String email;
    String studentId;
    String motherName;
    String password;
    String userType;
    String collegeName;

    public Registration() {
    }

    public Registration(Long id, String name, String email, String studentId, String motherName, String password, String userType, String collegeName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.motherName = motherName;
        this.password = password;
        this.userType = userType;
        this.collegeName = collegeName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", studentId='" + studentId + '\'' +
                ", motherName='" + motherName + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", collegeName='" + collegeName + '\'' +
                '}';
    }
}
