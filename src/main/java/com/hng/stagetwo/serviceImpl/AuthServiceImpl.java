package com.hng.stagetwo.serviceImpl;

import com.hng.stagetwo.dto.request.LoginRequestDto;
import com.hng.stagetwo.dto.request.RegistrationRequestDto;
import com.hng.stagetwo.dto.response.ResponseDto;
import com.hng.stagetwo.exception.exceptionHandler.EmailAlreadyExistException;
import com.hng.stagetwo.exception.exceptionHandler.InvalidCredentialException;
import com.hng.stagetwo.exception.exceptionHandler.UnsuccessfulRegistrationException;
import com.hng.stagetwo.jwt.service.JwtTokenService;
import com.hng.stagetwo.model.Organization;
import com.hng.stagetwo.model.User;
import com.hng.stagetwo.repository.OrganizationRepository;
import com.hng.stagetwo.repository.UserRepository;
import com.hng.stagetwo.service.AuthService;
import com.hng.stagetwo.service.OrganizationService;
import com.hng.stagetwo.utils.ConstantMessages;
import com.hng.stagetwo.utils.InfoGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InfoGetter infoGetter;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public ResponseEntity<?> registerUser(RegistrationRequestDto requestDto) {
            infoGetter.verifyEmail(requestDto.getEmail());
            User user = new User();
            user.setUserId(infoGetter.generateUniqueId());
            user.setFirstName(requestDto.getFirstName());
            user.setLastName(requestDto.getLastName());
            user.setEmail(requestDto.getEmail());
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            user.setPhone(requestDto.getPhone());
            user.setRole(ConstantMessages.ROLE_IS_USER.getMessage());
            Organization organization = organizationService.createDefaultOrganization(user.getFirstName(), user.getUserId());
            organizationRepository.save(organization);
            user.addOrganization(organization);
            User savedUser = userRepository.save(user);
            String token = jwtTokenService.createToken(user.getEmail(), user.getRoleAsList());
            ResponseDto responseDto = infoGetter.generateRegistrationResponse(savedUser, token);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    @Override
    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) throws InvalidCredentialException {
        User user = infoGetter.verifyLoginCredential(loginRequestDto.getEmail());
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialException(ConstantMessages.FAILED_AUTHENTICATION.getMessage());
        }
        String token = jwtTokenService.createToken(user.getEmail(), user.getRoleAsList());
        ResponseDto responseDto = infoGetter.generateLoginResponse(user, token);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
