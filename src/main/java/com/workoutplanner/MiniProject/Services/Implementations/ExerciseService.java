package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Models.Exercise;
import com.workoutplanner.MiniProject.Payload.Response.ExerciseResponse;
import com.workoutplanner.MiniProject.Repositories.ExerciseRepository;
import com.workoutplanner.MiniProject.Services.Interfaces.IExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService implements IExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Override
    public List<ExerciseResponse> getAllExercises() {
        List<Exercise> exercises = exerciseRepository.findAll();
        try {
            return exercises.stream().map(exercise -> {
                ExerciseResponse exerciseResponse = new ExerciseResponse();
                exerciseResponse.setId(exercise.getId());
                exerciseResponse.setName(exercise.getName());
                exerciseResponse.setDescription(exercise.getDescription());
                exerciseResponse.setCategory(exercise.getCategory().getCategoryName());
                return exerciseResponse;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
