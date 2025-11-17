package com.workoutplanner.MiniProject.Exception;

// Định nghĩa danh sách mã lỗi & message chuẩn.
// Quản lý tất cả mã lỗi một chỗ (thay vì hard-code trong nhiều nơi).
// “dictionary” cho tất cả lỗi trong app.
public enum ErrorCode {
    // ==============================
    // USER ERRORS (1xxx)
    // ==============================
    USER_EXISTED(1001, "User already exists"),
    PASSWORD_INVALID(1002, "Password must contain at least 8 characters"),
    ROLE_NOT_FOUND(1003, "Role not found"),
    USER_NOT_FOUND(1004, "User not found"),

    // ==============================
    // AUTHENTICATION ERRORS (2xxx)
    // ==============================
    UNAUTHENTICATED(2001, "Unauthenticated"),

    // ==============================
    // WORKOUT_PLAN_ERROR (3xxx)
    // ==============================
    WORKOUT_PLAN_NOT_EXISTED(3001, "Workout Plan not existed"),
    FORBIDDEN(3002, "You don't have permission to this plan"),

    // ==============================
    // WORKOUT_SCHEDULE_ERROR (4xxx)
    // ==============================
    WORKOUT_SCHEDULE_NOT_EXISTED(4001, "Workout Schedule not existed"),
    WORKOUT_SCHEDULE_FORBIDDEN(3002, "You don't have permission to this schedule"),

    // ==============================
    // WORKOUT_SCHEDULE_ERROR (5xxx)
    // ==============================
    EXERCISE_NOT_EXISTED(4001, "Exercise not existed"),

    // ==============================
    // SYSTEM ERRORS (9xxx)
    // ==============================
    UNCATEGORIZED(9999, "Uncategorized error"),
    DATABASE_ERROR(9001, "Database connection failed"),
    INTERNAL_SERVER_ERROR(9002, "Internal server error"),
    CONFIGURATION_ERROR(9003, "Configuration error");
    ;
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
