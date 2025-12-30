package com.workoutplanner.MiniProject.Controllers;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Payload.Response.ApiResponse;
import com.workoutplanner.MiniProject.Payload.Response.ExerciseResponse;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutExerciseProgressResponse;
import com.workoutplanner.MiniProject.Services.Implementations.WorkoutExerciseProgressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.WorkoutExerciseProgress.ROOT)
public class WorkoutExerciseProgressController {
    private final WorkoutExerciseProgressService workoutExerciseProgressService;

    public WorkoutExerciseProgressController(WorkoutExerciseProgressService workoutExerciseProgressService) {
        this.workoutExerciseProgressService = workoutExerciseProgressService;
    }

    @GetMapping("/exercise-progress")
    public ApiResponse<List<WorkoutExerciseProgressResponse>> getExercisesProgress() {
        ApiResponse<List<WorkoutExerciseProgressResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(workoutExerciseProgressService.getAllWorkoutExerciseProgress());
        apiResponse.setMessage("Get data successfully!");
        return apiResponse;
    }
}
