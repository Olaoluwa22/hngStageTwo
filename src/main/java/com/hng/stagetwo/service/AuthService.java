package com.hng.stagetwo.service;

import com.hng.stagetwo.dto.request.LoginRequestDto;
import com.hng.stagetwo.dto.request.RegistrationRequestDto;
import com.hng.stagetwo.exception.exceptionHandler.InvalidCredentialException;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> registerUser(RegistrationRequestDto requestDto);
    ResponseEntity<?> login(LoginRequestDto loginRequestDto) throws InvalidCredentialException;
}
