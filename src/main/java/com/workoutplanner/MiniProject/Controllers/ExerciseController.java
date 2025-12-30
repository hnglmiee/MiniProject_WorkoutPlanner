package com.workoutplanner.MiniProject.Controllers;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Payload.Response.ApiResponse;
import com.workoutplanner.MiniProject.Payload.Response.ExerciseResponse;
import com.workoutplanner.MiniProject.Services.Implementations.ExerciseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.Exercise.ROOT)
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/exercise")
    public ApiResponse<List<ExerciseResponse>> getExercises() {
        ApiResponse<List<ExerciseResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(exerciseService.getAllExercises());
        apiResponse.setMessage("Get data successfully!");
        return apiResponse;
    }
}
