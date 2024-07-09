package com.hng.stagetwo.serviceImpl;

import com.hng.stagetwo.dto.request.AddUserRequest;
import com.hng.stagetwo.dto.request.OrganizationRequestDto;
import com.hng.stagetwo.dto.response.OrganizationResponseDto;
import com.hng.stagetwo.exception.exceptionHandler.ResourceNotFoundException;
import com.hng.stagetwo.exception.exceptionHandler.UnsuccessfulCreationException;
import com.hng.stagetwo.model.Organization;
import com.hng.stagetwo.model.User;
import com.hng.stagetwo.repository.OrganizationRepository;
import com.hng.stagetwo.repository.UserRepository;
import com.hng.stagetwo.response.AddUserToOrgResponse;
import com.hng.stagetwo.response.ExceptionResponse;
import com.hng.stagetwo.response.OrganizationResponse;
import com.hng.stagetwo.response.OrganizationsResponse;
import com.hng.stagetwo.service.OrganizationService;
import com.hng.stagetwo.utils.ConstantMessages;
import com.hng.stagetwo.utils.InfoGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private InfoGetter infoGetter;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Organization createDefaultOrganization(String userFirstName, String userId) {
        Organization organization = new Organization();
        organization.setOrgId(infoGetter.generateOrganizationId());
        organization.setName(userFirstName +"'s organization");
        organization.setDescription("This organization was created by "+userFirstName +" with ID: "+userId);
        return organization;
    }
    @Override
    public ResponseEntity<?> getUserOrganizations() {
        User loggedInUser = infoGetter.getUser(infoGetter.getEmailOfLoggedInUser());
        List<Organization> userOrganizations = infoGetter.findOrganizationsByUserId(loggedInUser.getId());
        List<OrganizationResponseDto> organizationDtos = userOrganizations.stream().map(org ->
                new OrganizationResponseDto(org.getOrgId(), org.getName(), org.getDescription())
        ).collect(Collectors.toList());

        String status = ConstantMessages.SUCCESS.getMessage();
        String message = ConstantMessages.ORGANIZATIONS_LIST_RETRIEVED.getMessage();
        return new ResponseEntity<>(new OrganizationsResponse(status, message, organizationDtos), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> getOrganization(String orgId) {
        Organization organization = infoGetter.findByOrgId(orgId);
        User loggedInUser = infoGetter.getUser(infoGetter.getEmailOfLoggedInUser());
        if (!organization.getUsers().contains(loggedInUser)) {
            String status = ConstantMessages.UNAUTHORIZED.getMessage();
            String message = ConstantMessages.ACCESS_DENIED.getMessage();
            return new ResponseEntity<>(new ExceptionResponse<>(status, message, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }

        String status = ConstantMessages.SUCCESS.getMessage();
        String message = ConstantMessages.ORGANIZATION_DETAILS_RETRIEVED.getMessage();
        OrganizationResponse organizationResponse = new OrganizationResponse();
        organizationResponse.setStatus(status);
        organizationResponse.setMessage(message);
        organizationResponse.setData(organization);
        return new ResponseEntity<>(organizationResponse, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> createOrganization(OrganizationRequestDto requestDto) {
        try {
            User loggedInUser = infoGetter.getUser(infoGetter.getEmailOfLoggedInUser());
            Organization organization = new Organization();
            organization.setOrgId(infoGetter.generateOrganizationId());
            organization.setName(requestDto.getName());
            organization.setDescription(requestDto.getDescription());
            organization.addUser(loggedInUser);
            organizationRepository.save(organization);
            OrganizationResponse organizationResponse = new OrganizationResponse();
            organizationResponse.setStatus(ConstantMessages.SUCCESS.getMessage());
            organizationResponse.setMessage(ConstantMessages.ORGANIZATION_CREATED_SUCCESSFULLY.getMessage());
            organizationResponse.setData(organization);
            return new ResponseEntity<>(organizationResponse, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new UnsuccessfulCreationException(ConstantMessages.CLIENT_ERROR.getMessage());
    }
    @Override
    public ResponseEntity<?> addUserToOrganization(String orgId, AddUserRequest request) {
        Organization organization = organizationRepository.findByOrgId(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (organization.getUsers() instanceof Set) {
            Set<User> mutableUsers = new HashSet<>(organization.getUsers());
            mutableUsers.add(user);
            organization.setUsers(mutableUsers);
        } else {
            organization.getUsers().add(user);
        }

        organization.getUsers().add(user);
        user.addOrganization(organization);
        organizationRepository.save(organization);
        userRepository.save(user);
        String status = ConstantMessages.SUCCESS.getMessage();
        String message = ConstantMessages.USER_ADDED.getMessage();
        return new ResponseEntity<>(new AddUserToOrgResponse(status, message), HttpStatus.OK);
    }
}