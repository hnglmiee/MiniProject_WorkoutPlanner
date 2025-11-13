package com.workoutplanner.MiniProject.Payload.Request;

public class WorkoutPlanUpdateRequest {
    private String title;
    private String notes;

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
