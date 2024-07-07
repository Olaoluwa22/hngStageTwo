package com.hng.stagetwo.controller;

import com.hng.stagetwo.dto.request.LoginRequestDto;
import com.hng.stagetwo.dto.request.RegistrationRequestDto;
import com.hng.stagetwo.exception.exceptionHandler.InvalidCredentialException;
import com.hng.stagetwo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequestDto requestDto){
        return authService.registerUser(requestDto);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) throws InvalidCredentialException {
        return authService.login(loginRequestDto);
    }
}
