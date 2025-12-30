package com.workoutplanner.MiniProject.Services.Interfaces;

import com.workoutplanner.MiniProject.Payload.Response.ExerciseResponse;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutExerciseProgressResponse;

import java.util.List;

public interface IWorkoutExerciseProgressService {
    List<WorkoutExerciseProgressResponse> getAllWorkoutExerciseProgress();
}
