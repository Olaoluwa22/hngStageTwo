package com.hng.stagetwo.serviceImpl;

import com.hng.stagetwo.dto.UserDto;
import com.hng.stagetwo.dto.response.UserResponseDto;
import com.hng.stagetwo.response.UserErrorResponse;
import com.hng.stagetwo.model.User;
import com.hng.stagetwo.service.UserService;
import com.hng.stagetwo.utils.ConstantMessages;
import com.hng.stagetwo.utils.InfoGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private InfoGetter infoGetter;
    @Override
    public ResponseEntity<?> getUser(String id) {
        User user = infoGetter.findByUserId(id);
        User loggedInUser = infoGetter.getUser(infoGetter.getEmailOfLoggedInUser());
        if (!loggedInUser.getUserId().equals(id) && !loggedInUser.getOrganizations().containsAll(user.getOrganizations())) {
            String status = ConstantMessages.FORBIDDEN.getMessage();
            String message = ConstantMessages.ACCESS_DENIED.getMessage();
            return new ResponseEntity<>(new UserErrorResponse(status, message, 403), HttpStatus.FORBIDDEN);
        }
        String status = ConstantMessages.SUCCESS.getMessage();
        String message = ConstantMessages.USER_DETAILS_RETRIEVED.getMessage();
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());

        return new ResponseEntity<>(new UserResponseDto(status, message, userDto), HttpStatus.OK);
    }
}