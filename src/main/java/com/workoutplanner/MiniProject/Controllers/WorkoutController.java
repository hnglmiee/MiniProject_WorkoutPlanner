package com.workoutplanner.MiniProject.Controllers;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Payload.Request.WorkoutPlanRequest;
import com.workoutplanner.MiniProject.Payload.Request.WorkoutPlanUpdateRequest;
import com.workoutplanner.MiniProject.Payload.Response.ApiResponse;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutPlanResponse;
import com.workoutplanner.MiniProject.Services.Interfaces.IWorkoutPlanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.WorkoutPlan.ROOT)
public class WorkoutController {
    private final IWorkoutPlanService workoutPlanService;

    public WorkoutController(IWorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping
    public ApiResponse<List<WorkoutPlanResponse>> getAllWorkoutPlans() {
        ApiResponse<List<WorkoutPlanResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(workoutPlanService.getAllWorkoutPlan());
        apiResponse.setMessage("Get data successfully!");
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkoutPlanResponse> getAllWorkoutPlanById(@PathVariable int id) {
        ApiResponse<WorkoutPlanResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(workoutPlanService.getAllWorkoutPlanById(id));
        apiResponse.setMessage("Get data successfully!");
        return apiResponse;
    }

    @PostMapping()
    public ApiResponse<WorkoutPlanResponse> createWorkoutPlan(@RequestBody @Valid WorkoutPlanRequest request) {
        ApiResponse<WorkoutPlanResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(workoutPlanService.createWorkoutPlan(request));
        apiResponse.setMessage("Create data successfully!");
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<WorkoutPlanResponse> updateWorkoutPlan(@PathVariable Integer id, @RequestBody WorkoutPlanUpdateRequest request) {
        ApiResponse<WorkoutPlanResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(workoutPlanService.updateWorkoutPlan(id, request));
        apiResponse.setMessage("Update data successfully!");
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteWorkoutPlan(@PathVariable Integer id) {
        workoutPlanService.deleteWorkoutPlan(id);
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setResult(Boolean.TRUE);
        apiResponse.setMessage("Delete data successfully!");
        return apiResponse;
    }
}
