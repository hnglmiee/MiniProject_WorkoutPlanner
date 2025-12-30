package com.workoutplanner.MiniProject.Repositories;

import com.workoutplanner.MiniProject.Models.Exercise;
import com.workoutplanner.MiniProject.Models.ExerciseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseCategoryRepository extends JpaRepository<ExerciseCategory, Integer> {
    Optional<ExerciseCategory> findById(int id);
}