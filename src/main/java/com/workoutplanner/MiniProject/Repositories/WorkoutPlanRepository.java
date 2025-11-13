package com.workoutplanner.MiniProject.Repositories;

import com.workoutplanner.MiniProject.Models.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Integer> {
  Optional<WorkoutPlan> getWorkoutPlanById(int id);
  }