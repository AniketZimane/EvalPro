package com.example.EvalPro.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "subject_seq")
    @SequenceGenerator(name = "subject_seq")
    Long id;
    Long stream_id;
    Long sem_id;
    String sub_name;
    Boolean isElective;
    Boolean isPractical;
    Float PR_marks;
    Float TH_marks;
    Float TM_marks;
    Float total_marks;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Subject() {
    }


    public Subject(Long id, Long stream_id, Long sem_id, String sub_name, Boolean isElective, Boolean isPractical, Float PR_marks, Float TH_marks, Float TM_marks, Float total_marks, LocalDateTime createdDate) {
        this.id = id;
        this.stream_id = stream_id;
        this.sem_id = sem_id;
        this.sub_name = sub_name;
        this.isElective = isElective;
        this.isPractical = isPractical;
        this.PR_marks = PR_marks;
        this.TH_marks = TH_marks;
        this.TM_marks = TM_marks;
        this.total_marks = total_marks;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStream_id() {
        return stream_id;
    }

    public void setStream_id(Long stream_id) {
        this.stream_id = stream_id;
    }

    public Long getSem_id() {
        return sem_id;
    }

    public void setSem_id(Long sem_id) {
        this.sem_id = sem_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public Boolean getElective() {
        return isElective;
    }

    public void setElective(Boolean elective) {
        isElective = elective;
    }

    public Boolean getPractical() {
        return isPractical;
    }

    public void setPractical(Boolean practical) {
        isPractical = practical;
    }

    public Float getPR_marks() {
        return PR_marks;
    }

    public void setPR_marks(Float PR_marks) {
        this.PR_marks = PR_marks;
    }

    public Float getTH_marks() {
        return TH_marks;
    }

    public void setTH_marks(Float TH_marks) {
        this.TH_marks = TH_marks;
    }

    public Float getTM_marks() {
        return TM_marks;
    }

    public void setTM_marks(Float TM_marks) {
        this.TM_marks = TM_marks;
    }

    public Float getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(Float total_marks) {
        this.total_marks = total_marks;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", stream_id=" + stream_id +
                ", sem_id=" + sem_id +
                ", sub_name='" + sub_name + '\'' +
                ", isElective=" + isElective +
                ", isPractical=" + isPractical +
                ", PR_marks=" + PR_marks +
                ", TH_marks=" + TH_marks +
                ", TM_marks=" + TM_marks +
                ", total_marks=" + total_marks +
                ", createdDate=" + createdDate +
                '}';
    }
}
