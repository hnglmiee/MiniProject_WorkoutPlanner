package com.workoutplanner.MiniProject.Payload.Request;

import java.time.Instant;

public class WorkoutPlanRequest {
    private Integer userId;
    private String title;
    private String notes;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
