package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Exception.AppException;
import com.workoutplanner.MiniProject.Exception.ErrorCode;
import com.workoutplanner.MiniProject.Models.Exercise;
import com.workoutplanner.MiniProject.Models.User;
import com.workoutplanner.MiniProject.Models.WorkoutLog;
import com.workoutplanner.MiniProject.Models.WorkoutSchedule;
import com.workoutplanner.MiniProject.Payload.Request.WorkoutLogRequest;
import com.workoutplanner.MiniProject.Payload.Response.WorkoutLogResponse;
import com.workoutplanner.MiniProject.Repositories.ExerciseRepository;
import com.workoutplanner.MiniProject.Repositories.UserRepository;
import com.workoutplanner.MiniProject.Repositories.WorkoutLogRepository;
import com.workoutplanner.MiniProject.Repositories.WorkoutScheduleRepository;
import com.workoutplanner.MiniProject.Services.Interfaces.IWorkoutLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutLogService implements IWorkoutLogService {
    @Autowired
    private WorkoutLogRepository workoutLogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkoutScheduleRepository workoutScheduleRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;

    public WorkoutLogService(WorkoutLogRepository workoutLogRepository) {
        this.workoutLogRepository = workoutLogRepository;
    }

    @Override
    public List<WorkoutLogResponse> getAllWorkoutLog() {
        List<WorkoutLog> workoutLogs = workoutLogRepository.findAll();
        try {
            return workoutLogs.stream().map(workoutLog -> {
                WorkoutLogResponse response = new WorkoutLogResponse();
                response.setScheduleId(workoutLog.getSchedule().getId());
                response.setExerciseName(workoutLog.getExercise().getName());
                response.setActualSets(workoutLog.getActualSets());
                response.setActualReps(workoutLog.getActualReps());
                response.setActualWeight(workoutLog.getActualWeight());
                response.setNotes(workoutLog.getNotes());
                response.setLoggedAt(workoutLog.getLoggedAt());
                return response;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<WorkoutLogResponse> getMyWorkoutLog() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<WorkoutLog> workoutLogs = workoutLogRepository.findBySchedule_Plan_User(user);

        return workoutLogs.stream().map(workoutLog -> {
            WorkoutLogResponse response = new WorkoutLogResponse();
            response.setScheduleId(workoutLog.getSchedule().getId());
            response.setExerciseName(workoutLog.getExercise().getName());
            response.setActualSets(workoutLog.getActualSets());
            response.setActualReps(workoutLog.getActualReps());
            response.setActualWeight(workoutLog.getActualWeight());
            response.setNotes(workoutLog.getNotes());
            response.setLoggedAt(workoutLog.getLoggedAt());
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public WorkoutLogResponse createWorkoutLog(WorkoutLogRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Người dùng gửi scheduleId → kiểm tra schedule đó có tồn tại không.
        WorkoutSchedule schedule = workoutScheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new AppException(ErrorCode.WORKOUT_SCHEDULE_NOT_EXISTED));

        // Kiểm tra quyền sở hữu
        if(!schedule.getPlan().getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        // Tìm Exercise theo tên
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_EXISTED));

        // Request DTO -> Entity
        WorkoutLog workoutLog = new WorkoutLog();
        workoutLog.setSchedule(schedule);
        workoutLog.setExercise(exercise);
        workoutLog.setActualSets(request.getActualSets());
        workoutLog.setActualReps(request.getActualReps());
        workoutLog.setActualWeight(request.getActualWeight());
        workoutLog.setNotes(request.getNotes());
        workoutLog.setLoggedAt(Instant.now());

        workoutLogRepository.save(workoutLog);

        // Response DTO -> Entity
        WorkoutLogResponse response = new WorkoutLogResponse();
        response.setScheduleId(schedule.getId());
        response.setExerciseName(exercise.getName());
        response.setActualSets(workoutLog.getActualSets());
        response.setActualReps(workoutLog.getActualReps());
        response.setActualWeight(workoutLog.getActualWeight());
        response.setNotes(workoutLog.getNotes());
        response.setLoggedAt(workoutLog.getLoggedAt());
        return response;
    }

    @Override
    public WorkoutLogResponse updateWorkoutLog(Integer id, WorkoutLogRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        WorkoutSchedule workoutSchedule = workoutScheduleRepository.findById(request.getScheduleId()).orElseThrow(() -> new AppException(ErrorCode.WORKOUT_SCHEDULE_NOT_EXISTED));

        WorkoutLog workoutLog = workoutLogRepository.getWorkoutLogById(id).orElseThrow(() -> new AppException(ErrorCode.WORKOUT_LOG_NOT_EXITSED));

        if(!workoutSchedule.getPlan().getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        Exercise exercise = exerciseRepository.findById(request.getExerciseId()).orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_EXISTED));

        if(request.getExerciseId() != null) {
            workoutLog.setExercise(exercise);
        } if(request.getScheduleId() != null) {
            workoutLog.setSchedule(workoutSchedule);
        } if(request.getActualSets() != null) {
            workoutLog.setActualSets(request.getActualSets());
        } if(request.getActualReps() != null) {
            workoutLog.setActualReps(request.getActualReps());
        } if(request.getActualWeight() != null) {
            workoutLog.setActualWeight(request.getActualWeight());
        } if(request.getNotes() != null) {
            workoutLog.setNotes(request.getNotes());
        }
        workoutLogRepository.save(workoutLog);

        WorkoutLogResponse response = new WorkoutLogResponse();
        response.setScheduleId(workoutLog.getSchedule().getId());
        response.setExerciseName(workoutLog.getExercise().getName());
        response.setActualSets(workoutLog.getActualSets());
        response.setActualReps(workoutLog.getActualReps());
        response.setActualWeight(workoutLog.getActualWeight());
        response.setNotes(workoutLog.getNotes());
        response.setLoggedAt(workoutLog.getLoggedAt());
        return response;
    }

    @Override
    public boolean deleteWorkoutLog(Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        WorkoutLog workoutLog = workoutLogRepository.getWorkoutLogById(id).orElseThrow(() -> new AppException(ErrorCode.WORKOUT_LOG_NOT_EXITSED));

        Integer ownerId = workoutLog.getSchedule().getPlan().getUser().getId();

        if(!ownerId.equals(user.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        workoutLogRepository.delete(workoutLog);
        return true;
    }
}
