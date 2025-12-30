package com.workoutplanner.MiniProject.Services.Interfaces;

import com.workoutplanner.MiniProject.Payload.Response.ExerciseCategoriesResponse;
import com.workoutplanner.MiniProject.Payload.Response.ExerciseResponse;

import java.util.List;

public interface IExerciseCategoriesService {
    List<ExerciseCategoriesResponse> getAllExerciseCategories();
}
