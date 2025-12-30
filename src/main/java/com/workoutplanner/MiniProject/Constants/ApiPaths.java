package com.workoutplanner.MiniProject.Constants;

public class ApiPaths {
    // Ngan ko cho khoi tao class nay
    private ApiPaths() {}

    public static final String BASE_PATH = "/api/v1";

    // USER ENDPOINTS
    public static final class User {
        public static final String ROOT = BASE_PATH + "/users";
        public static final String GET_ALL = ROOT;
        public static final String CREATE = ROOT;
        public static final String UPDATE = ROOT + "/{id}";
        public static final String DELETE = ROOT + "/{id}";
        public static final String GET_BY_ID = ROOT + "/{id}";
    }

    // USER ENDPOINTS
    public static final class Auth {
        public static final String ROOT = BASE_PATH + "/auth";
        public static final String LOGIN = ROOT + "/login";
        public static final String REGISTER = ROOT + "/register";
    }

    // WORKOUT PLAN ENDPOINTS
    public static final class WorkoutPlan {
        public static final String ROOT = BASE_PATH + "/workout-plans";
    }

    // WORKOUT SCHEDULE ENDPOINTS
    public static final class WorkoutSchedule {
        public static final String ROOT = BASE_PATH + "/workout-schedules";
    }

    // WORKOUT LOG ENDPOINTS
    public static final class WorkoutLog {
        public static final String ROOT = BASE_PATH + "/workout-logs";
    }

    // USER IN BODY ENDPOINTS
    public static final class UserInBody {
        public static final String ROOT = BASE_PATH + "/user-in-body";
        public static final String IMPORT = BASE_PATH + ROOT + "/import";
    }

    // WORKOUT EXERCISE ENDPOINTS
    public static final class WorkoutExercise {
        public static final String ROOT = BASE_PATH + "/workout-exercise";
    }

    // EXERCISE ENDPOINTS
    public static final class Exercise {
        public static final String ROOT = BASE_PATH + "/exercise";
    }

    // EXERCISE CATEGORIES ENDPOINTS
    public static final class ExerciseCategory {
        public static final String ROOT = BASE_PATH + "/exercise-category";
    }

    // WORKOUT EXERCISE ENDPOINTS
    public static final class WorkoutExerciseProgress {
        public static final String ROOT = BASE_PATH + "/exercise-progress";
    }

    // USER GOAL ENDPOINTS
    public static final class UserGoal {
        public static final String ROOT = BASE_PATH + "/goal";
    }
}
