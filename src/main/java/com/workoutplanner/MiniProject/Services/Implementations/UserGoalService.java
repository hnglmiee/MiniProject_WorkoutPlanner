package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Exception.AppException;
import com.workoutplanner.MiniProject.Exception.ErrorCode;
import com.workoutplanner.MiniProject.Models.User;
import com.workoutplanner.MiniProject.Models.UserGoal;
import com.workoutplanner.MiniProject.Models.UserInbody;
import com.workoutplanner.MiniProject.Payload.Request.UserGoalRequest;
import com.workoutplanner.MiniProject.Payload.Response.UserGoalCreateResponse;
import com.workoutplanner.MiniProject.Payload.Response.UserGoalResponse;
import com.workoutplanner.MiniProject.Repositories.UserGoalRepository;
import com.workoutplanner.MiniProject.Repositories.UserInbodyRepository;
import com.workoutplanner.MiniProject.Repositories.UserRepository;
import com.workoutplanner.MiniProject.Repositories.WorkoutLogRepository;
import com.workoutplanner.MiniProject.Services.Interfaces.IUserGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class UserGoalService implements IUserGoalService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGoalRepository userGoalRepository;

    @Autowired
    private WorkoutLogRepository workoutLogRepository;

    @Autowired
    private UserInbodyRepository userInbodyRepository;

    @Override
    public UserGoalResponse checkGoalProgress() {
//        Lấy user hiện tai
        User user = getCurrentUser();

//        Lấy goal đang ACTIVE của user
        UserGoal goal = getActiveGoal(user);

//        Lấy bản inbody gần nhất
        UserInbody lastestInBody = getLastestInBody(user);

//        Đếm số buổi tập trong 7 ngày
        int loggedSession = countWorkoutSession(user);

//        Kiểm tra tiến độ
//        workoutOnTrack: đủ số buổi trong tuần
        boolean workoutOnTrack = evaluateWorkoutProgress(goal, loggedSession);
//        inBodyOnTrack: trọng lượng, bodyfat, muscle mass đạt yêu cầu
        boolean inBodyOntrack = evaluateInBodyProgress(goal, lastestInBody);

        UserGoalResponse response = buildResponse(goal, lastestInBody, loggedSession, workoutOnTrack, inBodyOntrack);

        // Kiểm tra hoàn thành mục tiêu
        if(goalFinished(goal, lastestInBody)) {
            response.setStatus("COMPLETED");
            userGoalRepository.save(goal);
            response.setStatus("COMPLETED");
        }
        return response;
    }

    @Override
    public UserGoalCreateResponse createUserGoal(UserGoalRequest request) {
        User user = getCurrentUser();

        UserGoal goal = new UserGoal();
        goal.setUser(user);
        goal.setGoalName(request.getGoalName());
        goal.setTargetWeight(request.getTargetWeight());
        goal.setTargetBodyFatPercentage(request.getTargetBodyFatPercentage());
        goal.setTargetMuscleMass(request.getTargetMuscleMass());
        goal.setTargetWorkoutSessionsPerWeek(request.getTargetWorkoutSessionsPerWeek());
        goal.setTargetCaloriesPerDay(request.getTargetCaloriesPerDay());
        goal.setStartDate(request.getStartDate());
        goal.setEndDate(request.getEndDate());
        goal.setStatus("ACTIVE");
        goal.setNotes(request.getNotes());

        userGoalRepository.save(goal);
        UserGoalCreateResponse response = new UserGoalCreateResponse();
        response.setGoalName(goal.getGoalName());
        response.setTargetWeight(goal.getTargetWeight());
        response.setTargetBodyFatPercentage(goal.getTargetBodyFatPercentage());
        response.setTargetMuscleMass(goal.getTargetMuscleMass());
        response.setTargetWorkoutSessionsPerWeek(goal.getTargetWorkoutSessionsPerWeek());
        response.setTargetCaloriesPerDay(goal.getTargetCaloriesPerDay());
        response.setStartDate(goal.getStartDate());
        response.setEndDate(goal.getEndDate());
        response.setNotes(goal.getNotes());
        response.setStatus(goal.getStatus());
        response.setNotes(goal.getNotes());
        return response;
    }

    @Override
    public UserGoalCreateResponse updateUserGoal(Integer id, UserGoalRequest request) {
        User user = getCurrentUser();
        UserGoal goal = userGoalRepository.getUserGoalById(id).orElseThrow(() -> new AppException(ErrorCode.GOAL_NOT_EXISTED));

        if(!goal.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.GOAL_NOT_EXISTED);
        }

        if(request.getGoalName() != null) {
            goal.setGoalName(request.getGoalName());
        } if(request.getTargetWeight() != null) {
            goal.setTargetWeight(request.getTargetWeight());
        } if(request.getTargetBodyFatPercentage() != null) {
            goal.setTargetBodyFatPercentage(request.getTargetBodyFatPercentage());
        } if(request.getTargetMuscleMass() != null) {
            goal.setTargetMuscleMass(request.getTargetMuscleMass());
        } if(request.getTargetWorkoutSessionsPerWeek() != null) {
            goal.setTargetWorkoutSessionsPerWeek(request.getTargetWorkoutSessionsPerWeek());
        } if(request.getTargetCaloriesPerDay() != null) {
            goal.setTargetCaloriesPerDay(request.getTargetCaloriesPerDay());
        } if(request.getStartDate() != null) {
            goal.setStartDate(request.getStartDate());
        } if(request.getEndDate() != null) {
            goal.setEndDate(request.getEndDate());
        } if(request.getNotes() != null) {
            goal.setNotes(request.getNotes());
        } if(request.getStatus() != null) {
            goal.setStatus(request.getStatus());
        }
        userGoalRepository.save(goal);

        UserGoalCreateResponse response = new UserGoalCreateResponse();
        response.setGoalName(goal.getGoalName());
        response.setTargetWeight(goal.getTargetWeight());
        response.setTargetBodyFatPercentage(goal.getTargetBodyFatPercentage());
        response.setTargetMuscleMass(goal.getTargetMuscleMass());
        response.setTargetWorkoutSessionsPerWeek(goal.getTargetWorkoutSessionsPerWeek());
        response.setTargetCaloriesPerDay(goal.getTargetCaloriesPerDay());
        response.setStartDate(goal.getStartDate());
        response.setEndDate(goal.getEndDate());
        response.setNotes(goal.getNotes());
        response.setStatus(goal.getStatus());
        return response;
    }

    //    Lấy user đang đăng nhập
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

//    Lấy goal ACTIVE mới nhất
    private UserGoal getActiveGoal(User user) {
        // Query tất cả goals của user có status "ACTIVE và .findFirst() → lấy goal đầu tiên
        return userGoalRepository.findTopByUserAndStatusOrderByStartDateDesc(user, "ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.GOAL_NOT_EXISTED));
    }

    private UserInbody getLastestInBody(User user) {
        // Check InBody Progress
        // Lấy bản ghi InBody mới nhất (ORDER BY measuredAt DESC)
        return userInbodyRepository.findTopByUserOrderByMeasuredAtDesc(user).orElseThrow(() -> new AppException(ErrorCode.USER_INBODY_NOT_FOUND));
    }

    private int countWorkoutSession(User user) {
        // Kiểm tra số buổi tập trong 7 ngày
        // weekStart = thời điểm 7 ngày trước
        Instant weekStart = Instant.now().minus(7, ChronoUnit.DAYS);
        // Query số buổi tập trong khoảng đó.
        // Repository sẽ count số buổi tập DISTINCT theo thời gian loggedAt.
        return workoutLogRepository.countWorkoutSessionInRange(user, weekStart, Instant.now());
    }

    private boolean evaluateWorkoutProgress(UserGoal goal, int loggedSessions) {
        // Lấy target session từ goal
        Integer targetSessions = goal.getTargetWorkoutSessionsPerWeek();

        // Nếu người dùng tập ít hơn target → workoutOnTrack = false.
        return targetSessions == null || targetSessions >= loggedSessions;
    }

    private boolean evaluateInBodyProgress(UserGoal goal, UserInbody inBody) {
        if (inBody == null) return false;
        if (isOverWeight(goal, inBody)) return false;
        if (isOverBodyFat(goal, inBody)) return false;
        if (isBelowMuscleMass(goal, inBody)) return false;
        return true;
    }

    private boolean isOverWeight(UserGoal goal, UserInbody inBody) {
        return goal.getTargetWeight() != null &&
                inBody.getWeight() != null && inBody.getWeight().compareTo(goal.getTargetWeight()) > 0;
    }

    private boolean isOverBodyFat(UserGoal goal, UserInbody inBody) {
        return goal.getTargetBodyFatPercentage() != null &&
                inBody.getBodyFatPercentage() != null && inBody.getBodyFatPercentage().compareTo(goal.getTargetBodyFatPercentage()) > 0;
    }

    private boolean isBelowMuscleMass(UserGoal goal, UserInbody inBody) {
        return goal.getTargetMuscleMass() != null &&
                inBody.getMuscleMass() != null && inBody.getMuscleMass().compareTo(goal.getTargetMuscleMass()) > 0;
    }

    // Kiểm tra hoàn thành mục tiêu
    private boolean goalFinished(UserGoal goal, UserInbody inbody) {
        if (inbody == null) return false;
        return !isOverWeight(goal, inbody) && !isOverBodyFat(goal, inbody) && !isBelowMuscleMass(goal, inbody);
    }

    private UserGoalResponse buildResponse(UserGoal goal, UserInbody lastestInBody, int loggedSessions, boolean workoutOnTrack, boolean inBodyOntrack) {
        UserGoalResponse response = new UserGoalResponse();

        // Tổng hợp trạng thái tiến độ
        // Nếu cả việc tập + số đo InBody ok → "ON_TRACK"
        if(workoutOnTrack && inBodyOntrack) {
            response.setStatus("ON_TRACK");
        } else {
            // Nếu 1 trong 2 sai → "BEHIND"
            response.setStatus("BEHIND");
        }

        response.setGoal(goal);
        response.setLastestInBody(lastestInBody);
        response.setWorkoutSessionThisWeek(loggedSessions);
        return response;
    }
}