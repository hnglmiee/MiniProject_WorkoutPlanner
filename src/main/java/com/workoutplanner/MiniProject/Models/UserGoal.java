package com.workoutplanner.MiniProject.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "UserGoals", schema = "workoutplanner")
public class UserGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GoalId", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "UserId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    // ----- Goal Name -----
    @Column(name = "GoalName", nullable = false, length = 100)
    private String goalName;

    // ----- Target InBody Metrics -----
    @Column(name = "TargetWeight", precision = 6, scale = 2)
    private BigDecimal targetWeight;

    @Column(name = "TargetBodyFatPercentage", precision = 5, scale = 2)
    private BigDecimal targetBodyFatPercentage;

    @Column(name = "TargetMuscleMass", precision = 6, scale = 2)
    private BigDecimal targetMuscleMass;

    // ----- Workout Goals -----
    @Column(name = "TargetWorkoutSessionsPerWeek")
    private Integer targetWorkoutSessionsPerWeek;

    @Column(name = "TargetCaloriesPerDay")
    private Integer targetCaloriesPerDay;

    // ----- Timeline -----
    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    // ACTIVE / COMPLETED / FAILED
    @Column(name = "Status", length = 20)
    private String status;

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

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public BigDecimal getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(BigDecimal targetWeight) {
        this.targetWeight = targetWeight;
    }

    public BigDecimal getTargetBodyFatPercentage() {
        return targetBodyFatPercentage;
    }

    public void setTargetBodyFatPercentage(BigDecimal targetBodyFatPercentage) {
        this.targetBodyFatPercentage = targetBodyFatPercentage;
    }

    public BigDecimal getTargetMuscleMass() {
        return targetMuscleMass;
    }

    public void setTargetMuscleMass(BigDecimal targetMuscleMass) {
        this.targetMuscleMass = targetMuscleMass;
    }

    public Integer getTargetWorkoutSessionsPerWeek() {
        return targetWorkoutSessionsPerWeek;
    }

    public void setTargetWorkoutSessionsPerWeek(Integer targetWorkoutSessionsPerWeek) {
        this.targetWorkoutSessionsPerWeek = targetWorkoutSessionsPerWeek;
    }

    public Integer getTargetCaloriesPerDay() {
        return targetCaloriesPerDay;
    }

    public void setTargetCaloriesPerDay(Integer targetCaloriesPerDay) {
        this.targetCaloriesPerDay = targetCaloriesPerDay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
