package com.workoutplanner.MiniProject.Repositories;

import com.workoutplanner.MiniProject.Models.WorkoutExerciseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutExerciseProgressRepository extends JpaRepository<WorkoutExerciseProgress, Integer> {
}