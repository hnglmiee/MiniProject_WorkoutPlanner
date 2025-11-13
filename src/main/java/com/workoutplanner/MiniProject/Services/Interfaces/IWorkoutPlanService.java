package com.workoutplanner.MiniProject.Services.Interfaces;

import com.workoutplanner.MiniProject.Models.WorkoutPlan;
import com.workoutplanner.MiniProject.Payload.Request.WorkoutPlanRequest;
import com.workoutplanner.MiniProject.Payload.Request.WorkoutPlanUpdateRequest;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutPlanResponse;

import java.util.List;

public interface IWorkoutPlanService {
    List<WorkoutPlanResponse> getAllWorkoutPlan();
    WorkoutPlanResponse getAllWorkoutPlanById(Integer id);
    WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request);
    WorkoutPlanResponse updateWorkoutPlan(Integer id, WorkoutPlanUpdateRequest request);
    boolean deleteWorkoutPlan(Integer id);
}
