package com.workoutplanner.MiniProject.Payload.Request;

import java.math.BigDecimal;
import java.time.Instant;

public class WorkoutLogRequest {
    private Integer scheduleId;
    private Integer exerciseId;
    private Integer actualSets;
    private Integer actualReps;
    private BigDecimal actualWeight;
    private String notes;
    private Instant loggedAt;

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getActualSets() {
        return actualSets;
    }

    public void setActualSets(Integer actualSets) {
        this.actualSets = actualSets;
    }

    public Integer getActualReps() {
        return actualReps;
    }

    public void setActualReps(Integer actualReps) {
        this.actualReps = actualReps;
    }

    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(Instant loggedAt) {
        this.loggedAt = loggedAt;
    }
}
