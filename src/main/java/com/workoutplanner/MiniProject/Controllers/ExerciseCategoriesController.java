package com.workoutplanner.MiniProject.Controllers;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Payload.Response.ApiResponse;
import com.workoutplanner.MiniProject.Payload.Response.ExerciseCategoriesResponse;
import com.workoutplanner.MiniProject.Payload.Response.ExerciseResponse;
import com.workoutplanner.MiniProject.Services.Implementations.ExerciseCategoriesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.Exercise.ROOT)
public class ExerciseCategoriesController {
    private final ExerciseCategoriesService exerciseCategoriesService;

    public ExerciseCategoriesController(ExerciseCategoriesService exerciseCategoriesService) {
        this.exerciseCategoriesService = exerciseCategoriesService;
    }

    @GetMapping("/exercise-categories")
    public ApiResponse<List<ExerciseCategoriesResponse>> getExerciseCategories() {
        ApiResponse<List<ExerciseCategoriesResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(exerciseCategoriesService.getAllExerciseCategories());
        apiResponse.setMessage("Get data successfully!");
        return apiResponse;
    }
}
