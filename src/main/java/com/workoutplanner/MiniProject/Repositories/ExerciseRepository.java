package com.workoutplanner.MiniProject.Repositories;

import com.workoutplanner.MiniProject.Models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
  Optional<Exercise> findByName(String name);
}