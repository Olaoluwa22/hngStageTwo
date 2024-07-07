package com.hng.stagetwo.response;

public class ExceptionResponse <Generic>{
    private String status;
    private String message;
    private int statusCode;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String status, String message, int statusCode) {
        this.status = status;
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
