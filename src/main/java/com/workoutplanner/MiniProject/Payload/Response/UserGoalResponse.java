package com.workoutplanner.MiniProject.Payload.Response;

import com.workoutplanner.MiniProject.Models.UserGoal;
import com.workoutplanner.MiniProject.Models.UserInbody;

public class UserGoalResponse {
    private String status;
    private Integer workoutSessionThisWeek;
    private UserInbody lastestInBody;
    private UserGoal goal;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getWorkoutSessionThisWeek() {
        return workoutSessionThisWeek;
    }

    public void setWorkoutSessionThisWeek(Integer workoutSessionThisWeek) {
        this.workoutSessionThisWeek = workoutSessionThisWeek;
    }

    public UserInbody getLastestInBody() {
        return lastestInBody;
    }

    public void setLastestInBody(UserInbody lastestInBody) {
        this.lastestInBody = lastestInBody;
    }

    public UserGoal getGoal() {
        return goal;
    }

    public void setGoal(UserGoal goal) {
        this.goal = goal;
    }
}
