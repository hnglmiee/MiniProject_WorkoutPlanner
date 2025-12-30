package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Models.ExerciseCategory;
import com.workoutplanner.MiniProject.Payload.Response.ExerciseCategoriesResponse;
import com.workoutplanner.MiniProject.Repositories.ExerciseCategoryRepository;
import com.workoutplanner.MiniProject.Services.Interfaces.IExerciseCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ExerciseCategoriesService implements IExerciseCategoriesService {
    @Autowired
    private ExerciseCategoryRepository exerciseCategoryRepository;

    @Override
    public List<ExerciseCategoriesResponse> getAllExerciseCategories() {
        List<ExerciseCategory> exerciseCategories = exerciseCategoryRepository.findAll();
        try {
            return exerciseCategories.stream().map(exerciseCategory -> {
                ExerciseCategoriesResponse exerciseCategoriesResponse = new ExerciseCategoriesResponse();
                exerciseCategoriesResponse.setId(exerciseCategory.getId());
                exerciseCategoriesResponse.setCategoryName(exerciseCategory.getCategoryName());
                exerciseCategoriesResponse.setDescription(exerciseCategory.getDescription());
                return exerciseCategoriesResponse;
            }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
