package com.workoutplanner.MiniProject.Services.Interfaces;


import com.workoutplanner.MiniProject.Payload.Response.WorkoutExerciseResponse;

import java.util.List;

public interface IWorkoutExerciseService {
    List<WorkoutExerciseResponse> getAllWorkoutExercises();
    List<WorkoutExerciseResponse> getMyWorkoutExercises();
}
