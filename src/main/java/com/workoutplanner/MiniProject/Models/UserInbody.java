package com.workoutplanner.MiniProject.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "UserInbody", schema = "workoutplanner")
public class UserInbody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InbodyId", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserId", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "Age")
    private Integer age;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "MeasuredAt")
    private Instant measuredAt;

    @Column(name = "Height", precision = 5, scale = 2)
    private BigDecimal height;

    @Column(name = "Weight", precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "BodyFatPercentage", precision = 5, scale = 2)
    private BigDecimal bodyFatPercentage;

    @Column(name = "MuscleMass", precision = 6, scale = 2)
    private BigDecimal muscleMass;

    @Column(name = "Notes")
    private String notes;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(Instant measuredAt) {
        this.measuredAt = measuredAt;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(BigDecimal bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public BigDecimal getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(BigDecimal muscleMass) {
        this.muscleMass = muscleMass;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}