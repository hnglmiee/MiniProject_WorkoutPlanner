package com.workoutplanner.MiniProject.Controllers;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Payload.Response.ApiResponse;
import com.workoutplanner.MiniProject.Payload.Response.UserGoalResponse;
import com.workoutplanner.MiniProject.Services.Implementations.UserGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.UserGoal.ROOT)
public class UserGoalController {
    @Autowired
    private UserGoalService userGoalService;

    @GetMapping("/progress")
    public ApiResponse<UserGoalResponse> checkGoalProgress() {
        ApiResponse<UserGoalResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userGoalService.checkGoalProgress());
        apiResponse.setMessage("Get data successfully!");
        return apiResponse;
    }
}
