package com.hng.stagetwo.utils;

public enum ConstantMessages {
    SUCCESS("Success", 1),
    REGISTRATION_SUCCESSFUL("Registration Successful", 2),
    EMAIL_EXIST("Email Already in Use", 3),
    ROLE_IS_USER("User", 4),
    INVALID_CREDENTIAL("Incorrect Username or Password", 5),
    REGISTRATION_UNSUCCESSFUL("Registration unsuccessful", 6),
    FAILED_AUTHENTICATION("Authentication failed", 7),
    BAD_REQUEST("Bad request", 8),
    LOGIN_SUCCESSFUL("Login successful", 9),
    USER_NOT_FOUND("User not found", 10),
    USER_DETAILS_RETRIEVED("User details retrieved", 11),
    FORBIDDEN("Forbidden", 12),
    ACCESS_DENIED("Access denied", 13),
    ORGANIZATIONS_LIST_RETRIEVED("Organizations retrieved", 14),
    RESOURCE_NOT_FOUND("Resource not found", 15),
    NOT_FOUND("Not found", 16),
    ORGANIZATION_DETAILS_RETRIEVED("Organization details retrieved", 17),
    UNAUTHORIZED("Unauthorized", 18),
    ORGANIZATION_CREATED_SUCCESSFULLY("Organization created successfully", 19),
    CLIENT_ERROR("Client error", 20),
    USER_ADDED("User added to organization successfully", 21);
    private String message;
    private int status;
    ConstantMessages(String message, int status){
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConstantMessages{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
