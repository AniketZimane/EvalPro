package com.example.EvalPro.Entity;

import jakarta.persistence.*;

@Entity
public class RevaluationRequest {
    @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "revaluation_seq")
            @SequenceGenerator(name = "revaluation_seq")
    Long id;
    String fName;
    String seatNumber;
    String email;
    String phone;
    String course;
    String semester;
    @Column(length = 1000) // Increase size to store multiple subjects
    private String subjects; // Store as comma-separated values (CSV) or JSON
    String reason;
    String isPayment;
    String isVerify;
    String isEvaluate;
    String evaluaterId;
    String isModerate;
    String moderaterId;
    String remark;

    public RevaluationRequest() {
    }

    public RevaluationRequest(Long id, String fName, String seatNumber, String email, String phone, String course, String semester, String subjects, String reason, String isPayment, String isVerify, String isEvaluate, String evaluaterId, String isModerate, String moderaterId, String remark) {
        this.id = id;
        this.fName = fName;
        this.seatNumber = seatNumber;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.semester = semester;
        this.subjects = subjects;
        this.reason = reason;
        this.isPayment = isPayment;
        this.isVerify = isVerify;
        this.isEvaluate = isEvaluate;
        this.evaluaterId = evaluaterId;
        this.isModerate = isModerate;
        this.moderaterId = moderaterId;
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(String isPayment) {
        this.isPayment = isPayment;
    }

    public String getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(String isVerify) {
        this.isVerify = isVerify;
    }

    public String getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(String isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public String getEvaluaterId() {
        return evaluaterId;
    }

    public void setEvaluaterId(String evaluaterId) {
        this.evaluaterId = evaluaterId;
    }

    public String getIsModerate() {
        return isModerate;
    }

    public void setIsModerate(String isModerate) {
        this.isModerate = isModerate;
    }

    public String getModeraterId() {
        return moderaterId;
    }

    public void setModeraterId(String moderaterId) {
        this.moderaterId = moderaterId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "RevaluationRequest{" +
                "id=" + id +
                ", fName='" + fName + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", course='" + course + '\'' +
                ", semester='" + semester + '\'' +
                ", subjects='" + subjects + '\'' +
                ", reason='" + reason + '\'' +
                ", isPayment='" + isPayment + '\'' +
                ", isVerify='" + isVerify + '\'' +
                ", isEvaluate='" + isEvaluate + '\'' +
                ", evaluaterId='" + evaluaterId + '\'' +
                ", isModerate='" + isModerate + '\'' +
                ", moderaterId='" + moderaterId + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
