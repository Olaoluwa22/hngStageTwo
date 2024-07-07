package com.hng.stagetwo.utils;

import com.hng.stagetwo.dto.UserDto;

public class Data {
    private String accessToken;
    private UserDto user;

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public UserDto getUser() {
        return user;
    }
    public void setUser(UserDto user) {
        this.user = user;
    }
}
