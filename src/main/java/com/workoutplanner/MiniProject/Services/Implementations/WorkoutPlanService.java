package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Exception.AppException;
import com.workoutplanner.MiniProject.Exception.ErrorCode;
import com.workoutplanner.MiniProject.Models.User;
import com.workoutplanner.MiniProject.Models.WorkoutPlan;
import com.workoutplanner.MiniProject.Payload.Request.WorkoutPlanRequest;
import com.workoutplanner.MiniProject.Payload.Request.WorkoutPlanUpdateRequest;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutPlanResponse;
import com.workoutplanner.MiniProject.Repositories.UserRepository;
import com.workoutplanner.MiniProject.Repositories.WorkoutPlanRepository;
import com.workoutplanner.MiniProject.Services.Interfaces.IWorkoutPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutPlanService implements IWorkoutPlanService {
    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;
    @Autowired
    private UserRepository userRepository;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    @Override
    public List<WorkoutPlanResponse> getAllWorkoutPlan() {
        List<WorkoutPlan> workoutPlans = workoutPlanRepository.findAll();
        try {
            return workoutPlans.stream().map(workoutPlan -> {
                WorkoutPlanResponse workoutPlanResponse = new WorkoutPlanResponse();
                workoutPlanResponse.setId(workoutPlan.getId());
                workoutPlanResponse.setFullName(workoutPlan.getUser().getFullName());
                workoutPlanResponse.setTitle(workoutPlan.getTitle());
                workoutPlanResponse.setNotes(workoutPlan.getNotes());
                workoutPlanResponse.setCreatedAt(workoutPlan.getCreatedAt());
                workoutPlanResponse.setUpdatedAt(workoutPlan.getUpdatedAt());
                return workoutPlanResponse;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WorkoutPlanResponse getAllWorkoutPlanById(Integer id) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WORKOUT_PLAN_NOT_EXISTED));
        WorkoutPlanResponse workoutPlanResponse = new WorkoutPlanResponse();
        workoutPlanResponse.setId(workoutPlan.getId());
        workoutPlanResponse.setFullName(workoutPlan.getUser().getFullName());
        workoutPlanResponse.setTitle(workoutPlan.getTitle());
        workoutPlanResponse.setNotes(workoutPlan.getNotes());
        workoutPlanResponse.setCreatedAt(workoutPlan.getCreatedAt());
        workoutPlanResponse.setUpdatedAt(workoutPlan.getUpdatedAt());
        return workoutPlanResponse;
    }

    @Override
    public WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request) {
        // Lay user tu DB
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // DTO request -> Entity
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setUser(user); // user lấy từ DB dựa trên userId trong request
        workoutPlan.setTitle(request.getTitle());
        workoutPlan.setNotes(request.getNotes());
        workoutPlan.setCreatedAt(Instant.now());

        // Luu vao DB
        WorkoutPlan savedPlan = workoutPlanRepository.save(workoutPlan);

        // Entity -> DTO response
        WorkoutPlanResponse workoutPlanResponse = new WorkoutPlanResponse();
        workoutPlanResponse.setId(savedPlan.getId());
        workoutPlanResponse.setFullName(workoutPlan.getUser().getFullName());
        workoutPlanResponse.setTitle(workoutPlan.getTitle());
        workoutPlanResponse.setNotes(workoutPlan.getNotes());
        workoutPlanResponse.setCreatedAt(workoutPlan.getCreatedAt());
        workoutPlanResponse.setUpdatedAt(workoutPlan.getUpdatedAt());
        return workoutPlanResponse;
    }

    @Override
    public WorkoutPlanResponse updateWorkoutPlan(Integer id, WorkoutPlanUpdateRequest request) {
        WorkoutPlan workoutPlan = workoutPlanRepository.getWorkoutPlanById(id).orElseThrow(() -> new AppException(ErrorCode.WORKOUT_PLAN_NOT_EXISTED));

        // Request DTO -> Entity
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            workoutPlan.setTitle(request.getTitle());
        } if (request.getNotes() != null) {
            workoutPlan.setNotes(request.getNotes());
        }
        workoutPlan.setUpdatedAt(Instant.now());

        // Luu vao DB
        WorkoutPlan updatedPlan = workoutPlanRepository.save(workoutPlan);

        WorkoutPlanResponse workoutPlanResponse = new WorkoutPlanResponse();
        workoutPlanResponse.setId(updatedPlan.getId());
        workoutPlanResponse.setFullName(updatedPlan.getUser().getFullName());
        workoutPlanResponse.setTitle(updatedPlan.getTitle());
        workoutPlanResponse.setNotes(updatedPlan.getNotes());
        workoutPlanResponse.setCreatedAt(updatedPlan.getCreatedAt());
        workoutPlanResponse.setUpdatedAt(updatedPlan.getUpdatedAt());
        return workoutPlanResponse;
    }

    @Override
    public boolean deleteWorkoutPlan(Integer id) {
        WorkoutPlan workoutPlan = workoutPlanRepository.getWorkoutPlanById(id).orElseThrow(() -> new AppException(ErrorCode.WORKOUT_PLAN_NOT_EXISTED));
        workoutPlanRepository.delete(workoutPlan);
        return true;
    }
}
