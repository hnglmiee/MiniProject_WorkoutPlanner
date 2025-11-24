package com.workoutplanner.MiniProject.Repositories;

import com.workoutplanner.MiniProject.Models.User;
import com.workoutplanner.MiniProject.Models.UserGoal;
import com.workoutplanner.MiniProject.Models.UserInbody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGoalRepository extends JpaRepository<UserGoal, Integer> {
    Optional<UserGoal> findTopByUserAndStatusOrderByStartDateDesc(User user, String status);
}