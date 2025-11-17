package com.workoutplanner.MiniProject.Repositories;

import com.workoutplanner.MiniProject.Models.User;
import com.workoutplanner.MiniProject.Models.WorkoutSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutScheduleRepository extends JpaRepository<WorkoutSchedule, Integer> {
    List<WorkoutSchedule> findByPlan_User(User user);
    Optional<WorkoutSchedule> findByPlan_Id(Integer planId);
    Optional<WorkoutSchedule> getWorkoutScheduleById(Integer id);
    Optional<WorkoutSchedule> findById(Integer id);
}