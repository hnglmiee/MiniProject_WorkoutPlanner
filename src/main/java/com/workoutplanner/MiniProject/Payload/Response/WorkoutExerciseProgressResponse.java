package com.workoutplanner.MiniProject.Payload.Response;

import java.time.Instant;
import java.time.LocalDate;

public class WorkoutExerciseProgressResponse {
    private Integer id;
    private LocalDate schedule;
    private String workoutExercise;
    private Boolean isCompleted;
    private Instant completedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getSchedule() {
        return schedule;
    }

    public void setSchedule(LocalDate schedule) {
        this.schedule = schedule;
    }

    public String getWorkoutExercise() {
        return workoutExercise;
    }

    public void setWorkoutExercise(String workoutExercise) {
        this.workoutExercise = workoutExercise;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}
