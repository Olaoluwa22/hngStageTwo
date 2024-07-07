package com.hng.stagetwo.utils;

import com.hng.stagetwo.dto.UserDto;
import com.hng.stagetwo.dto.response.ResponseDto;
import com.hng.stagetwo.exception.exceptionHandler.EmailAlreadyExistException;
import com.hng.stagetwo.exception.exceptionHandler.InvalidCredentialException;
import com.hng.stagetwo.exception.exceptionHandler.ResourceNotFoundException;
import com.hng.stagetwo.model.Organization;
import com.hng.stagetwo.model.User;
import com.hng.stagetwo.repository.OrganizationRepository;
import com.hng.stagetwo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class InfoGetter {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    public User findByUserId(String id){
        Optional<User> optionalUser = userRepository.findByUserId(id);
        if (optionalUser.isEmpty()){
            throw new BadCredentialsException("User with ID_"+id+ " not found");
        }
        return optionalUser.get();
    }
    public void verifyEmail(String email){
        Optional<User> optionalEmail = userRepository.findByEmail(email);
        if (optionalEmail.isPresent()){
            throw new EmailAlreadyExistException(ConstantMessages.EMAIL_EXIST.getMessage());
        }
    }
    public User verifyLoginCredential(String email) throws InvalidCredentialException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new InvalidCredentialException(ConstantMessages.FAILED_AUTHENTICATION.getMessage());
        }
        return optionalUser.get();
    }
    public ResponseDto generateRegistrationResponse(User user, String jwtToken){
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        Data data = new Data();
        data.setAccessToken(jwtToken);
        data.setUser(userDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(ConstantMessages.SUCCESS.getMessage());
        responseDto.setMessage(ConstantMessages.REGISTRATION_SUCCESSFUL.getMessage());
        responseDto.setData(data);
        return responseDto;
    }
    public ResponseDto generateLoginResponse(User user, String jwtToken){
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        Data data = new Data();
        data.setAccessToken(jwtToken);
        data.setUser(userDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(ConstantMessages.SUCCESS.getMessage());
        responseDto.setMessage(ConstantMessages.LOGIN_SUCCESSFUL.getMessage());
        responseDto.setData(data);
        return responseDto;
    }
    public String generateUniqueId(){
        return generateRandomPrefix() +"-"+ generateRandomThreeDigits();
    }
    public String generateOrganizationId(){
        return generateRandomPrefix() +"_"+ generateRandomFiveDigits();
    }
    private static String generateRandomPrefix(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            prefix.append(characters.charAt(random.nextInt(characters.length())));
        }
        return prefix.toString();
    }
    private static String generateRandomThreeDigits(){
        return String.valueOf(ThreadLocalRandom.current().nextInt(100, 999));
    }
    private static String generateRandomFiveDigits(){
        return String.valueOf(ThreadLocalRandom.current().nextInt(10000, 99999));
    }
    public String getEmailOfLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null & authentication.getPrincipal() instanceof UserDetails){
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return "INTERNAL SERVER ERROR...";
    }
    public User getUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()){
            throw new BadCredentialsException(ConstantMessages.USER_NOT_FOUND.getMessage());
        }
        return optionalUser.get();
    }
    public List<Organization> findOrganizationsByUserId(Long id) {
        return organizationRepository.findByUsers_Id(id);
    }
    public Organization findByOrgId(String orgId) {
        return organizationRepository.findByOrgId(orgId).orElseThrow(()
                -> new ResourceNotFoundException("Organization not found"));
    }
}