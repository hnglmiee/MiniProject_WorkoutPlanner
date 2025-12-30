package com.workoutplanner.MiniProject.Services.Interfaces;

import com.workoutplanner.MiniProject.Models.Exercise;
import com.workoutplanner.MiniProject.Payload.Response.ExerciseResponse;

import java.util.List;

public interface IExerciseService {
    List<ExerciseResponse> getAllExercises();
}
