package com.workoutplanner.MiniProject.Services.Interfaces;

import com.workoutplanner.MiniProject.Payload.Request.UserGoalRequest;
import com.workoutplanner.MiniProject.Payload.Response.UserGoalCreateResponse;
import com.workoutplanner.MiniProject.Payload.Response.UserGoalResponse;

public interface IUserGoalService {
    UserGoalResponse checkGoalProgress();
    UserGoalCreateResponse createUserGoal(UserGoalRequest request);
    UserGoalCreateResponse updateUserGoal(Integer id, UserGoalRequest request);
}
