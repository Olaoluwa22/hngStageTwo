package com.hng.stagetwo.dto.response;

import com.hng.stagetwo.dto.UserDto;

public class UserResponseDto {
    private String status;
    private String message;
    private UserDto data;

    public UserResponseDto(String status, String message, UserDto data) {
        this.status = status;
        this.message = message;
        this.data = data;
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
    public UserDto getData() {
        return data;
    }
    public void setData(UserDto data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
