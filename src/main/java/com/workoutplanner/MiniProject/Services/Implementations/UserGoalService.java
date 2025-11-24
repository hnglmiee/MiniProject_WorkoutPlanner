package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Exception.AppException;
import com.workoutplanner.MiniProject.Exception.ErrorCode;
import com.workoutplanner.MiniProject.Models.User;
import com.workoutplanner.MiniProject.Models.UserGoal;
import com.workoutplanner.MiniProject.Models.UserInbody;
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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Query tất cả goals của user có status "ACTIVE và .findFirst() → lấy goal đầu tiên
        UserGoal goal = userGoalRepository.findTopByUserAndStatusOrderByStartDateDesc(user, "ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.GOAL_NOT_EXISTED));

        UserGoalResponse response = new UserGoalResponse();

        // Kiểm tra số buổi tập trong 7 ngày
        // weekStart = thời điểm 7 ngày trước
        Instant weekStart = Instant.now().minus(7, ChronoUnit.DAYS);
        // now = hiện tại
        Instant now = Instant.now();

        // Repository sẽ count số buổi tập DISTINCT theo thời gian loggedAt.
        int loggedSession = workoutLogRepository.countWorkoutSessionInRange(user, weekStart, now);

        // Lấy target session từ goal
        Integer targetSessions = goal.getTargetWorkoutSessionsPerWeek();

        // Nếu người dùng tập ít hơn số buổi mục tiêu → workoutOnTrack = false.
        boolean workoutOnTrack = true;
        if(targetSessions != null && loggedSession < targetSessions) {
            workoutOnTrack = false;
        }

        // Check InBody Progress
        // Lấy bản ghi InBody mới nhất (ORDER BY measuredAt DESC)
        UserInbody lastestInBody = userInbodyRepository.findTopByUserOrderByMeasuredAtDesc(user).orElseThrow(() -> new AppException(ErrorCode.USER_INBODY_NOT_FOUND));

        // Mặc định InBody ban đầu on track
        boolean inBodyOntrack = true;

        if(lastestInBody != null) {
            // Nếu weight hiện tại > target weight → fail.
            if(goal.getTargetWeight() != null && lastestInBody.getWeight() != null && lastestInBody.getWeight().compareTo(goal.getTargetWeight()) > 0) {
                inBodyOntrack = false;
            }

            if(goal.getTargetBodyFatPercentage() != null &&
                    // Nếu body fat hiện tại > target body fat → fail.
                    lastestInBody.getBodyFatPercentage() != null &&
                    lastestInBody.getBodyFatPercentage().compareTo(goal.getTargetBodyFatPercentage()) > 0) {
                inBodyOntrack = false;
            }

            if(goal.getTargetMuscleMass() != null && lastestInBody.getMuscleMass() != null && lastestInBody.getMuscleMass().compareTo(goal.getTargetMuscleMass()) < 0) {
                // Nếu muscle mass hiện tại < mục tiêu → fail.
                inBodyOntrack = false;
            }
        }

        // Tổng hợp trạng thái tiến độ
        // Nếu cả việc tập + số đo InBody ok → "ON_TRACK"
        if(workoutOnTrack && inBodyOntrack) {
            response.setStatus("ON_TRACK");
        } else {
            // Nếu 1 trong 2 sai → "BEHIND"
            response.setStatus("BEHIND");
        }

        response.setWorkoutSessionThisWeek(loggedSession);
        response.setLastestInBody(lastestInBody);
        response.setGoal(goal);

        // Kiểm tra xem đã đạt mục tiêu chưa
        if(goalFinished(goal, lastestInBody)) {
            response.setStatus("COMPLETED");
            userGoalRepository.save(goal);
            response.setStatus("COMPLETED");
        }
        return response;
    }

    // Kiểm tra hoàn thành mục tiêu
    private boolean goalFinished(UserGoal goal, UserInbody inbody) {
        if (inbody == null) return false;

        boolean finished = true;
        if (goal.getTargetWeight() != null &&
                inbody.getWeight().compareTo(goal.getTargetWeight()) > 0) {
            finished = false;
        }
        if (goal.getTargetBodyFatPercentage() != null &&
                inbody.getBodyFatPercentage().compareTo(goal.getTargetBodyFatPercentage()) > 0) {
            finished = false;
        }
        if (goal.getTargetMuscleMass() != null &&
                inbody.getMuscleMass().compareTo(goal.getTargetMuscleMass()) < 0) {
            finished = false;
        }

        return finished;
    }
}