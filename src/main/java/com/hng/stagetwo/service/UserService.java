package com.hng.stagetwo.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getUser(String id);
}
