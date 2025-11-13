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
    // USER ERRORS (1xxx)
    // ==============================
    WORKOUT_PLAN_NOT_EXISTED(3001, "Workout Plan not existed"),

    // ==============================
    // AUTHENTICATION ERRORS (1xxx)
    // ==============================
    UNAUTHENTICATED(2001, "Unauthenticated"),

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
