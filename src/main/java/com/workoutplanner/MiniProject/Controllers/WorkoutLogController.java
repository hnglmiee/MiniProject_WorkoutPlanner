package com.workoutplanner.MiniProject.Controllers;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Payload.Request.WorkoutLogRequest;
import com.workoutplanner.MiniProject.Payload.Request.WorkoutPlanRequest;
import com.workoutplanner.MiniProject.Payload.Response.ApiResponse;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutLogResponse;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutPlanResponse;
import com.workoutplanner.MiniProject.Services.Interfaces.IWorkoutLogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.WorkoutLog.ROOT)
public class WorkoutLogController {
    private final IWorkoutLogService workoutLogService;

    public WorkoutLogController(IWorkoutLogService workoutLogService) {
        this.workoutLogService = workoutLogService;
    }

    @GetMapping("/admin")
    public ApiResponse <List<WorkoutLogResponse>> getWorkoutLogs() {
        ApiResponse<List<WorkoutLogResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(workoutLogService.getAllWorkoutLog());
        apiResponse.setMessage("Get data successfully!");
        return apiResponse;
    }

    @GetMapping("/my-workout-logs")
    public ApiResponse <List<WorkoutLogResponse>> getMyWorkoutLogs() {
        ApiResponse<List<WorkoutLogResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(workoutLogService.getMyWorkoutLog());
        apiResponse.setMessage("Get data successfully!");
        return apiResponse;
    }

    @PostMapping()
    public ApiResponse<WorkoutLogResponse> createWorkoutLog(@RequestBody @Valid WorkoutLogRequest request) {
        ApiResponse<WorkoutLogResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(workoutLogService.createWorkoutLog(request));
        apiResponse.setMessage("Create data successfully!");
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<WorkoutLogResponse> updateWorkoutLog(@PathVariable Integer id, @RequestBody @Valid WorkoutLogRequest request) {
        ApiResponse<WorkoutLogResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(workoutLogService.updateWorkoutLog(id, request));
        apiResponse.setMessage("Update data successfully!");
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteWorkoutLog(@PathVariable Integer id) {
        workoutLogService.deleteWorkoutLog(id);
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setResult(Boolean.TRUE);
        apiResponse.setMessage("Delete data successfully!");
        return apiResponse;
    }
}
