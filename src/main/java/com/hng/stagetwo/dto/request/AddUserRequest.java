package com.hng.stagetwo.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AddUserRequest {
    @NotBlank
    private String userId;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AddUserRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
