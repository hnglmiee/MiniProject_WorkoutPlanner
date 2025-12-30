package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Models.WorkoutExerciseProgress;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutExerciseProgressResponse;
import com.workoutplanner.MiniProject.Repositories.WorkoutExerciseProgressRepository;
import com.workoutplanner.MiniProject.Repositories.WorkoutScheduleRepository;
import com.workoutplanner.MiniProject.Services.Interfaces.IWorkoutExerciseProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutExerciseProgressService implements IWorkoutExerciseProgressService {
    @Autowired
    private WorkoutExerciseProgressRepository workoutExerciseProgressRepository;

    @Override
    public List<WorkoutExerciseProgressResponse> getAllWorkoutExerciseProgress() {
        List<WorkoutExerciseProgress> exerciseProgress = workoutExerciseProgressRepository.findAll();
        try {
            return exerciseProgress.stream().map(exercise -> {
                WorkoutExerciseProgressResponse workoutExerciseProgressResponse = new WorkoutExerciseProgressResponse();
                workoutExerciseProgressResponse.setId(exercise.getId());
                workoutExerciseProgressResponse.setSchedule(exercise.getSchedule().getScheduledDate());
                workoutExerciseProgressResponse.setWorkoutExercise(exercise.getWorkoutExercise().getExercise().getName());
                workoutExerciseProgressResponse.setCompleted(exercise.getCompleted());
                workoutExerciseProgressResponse.setCompletedAt(exercise.getCompletedAt());
                return workoutExerciseProgressResponse;
            }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
