package com.workoutplanner.MiniProject.Services.Interfaces;

import com.workoutplanner.MiniProject.Payload.Request.WorkoutLogRequest;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutLogResponse;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutPlanResponse;

import java.util.List;

public interface IWorkoutLogService {
    List<WorkoutLogResponse> getAllWorkoutLog();
    List<WorkoutLogResponse> getMyWorkoutLog();
    WorkoutLogResponse createWorkoutLog(WorkoutLogRequest request);
}
