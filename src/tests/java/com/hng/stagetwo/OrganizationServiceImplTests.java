package com.hng.stagetwo;

import com.hng.stagetwo.dto.request.AddUserRequest;
import com.hng.stagetwo.dto.request.OrganizationRequestDto;
import com.hng.stagetwo.dto.response.OrganizationResponseDto;
import com.hng.stagetwo.exception.exceptionHandler.ResourceNotFoundException;
import com.hng.stagetwo.model.Organization;
import com.hng.stagetwo.model.User;
import com.hng.stagetwo.repository.OrganizationRepository;
import com.hng.stagetwo.repository.UserRepository;
import com.hng.stagetwo.response.AddUserToOrgResponse;
import com.hng.stagetwo.response.ExceptionResponse;
import com.hng.stagetwo.response.OrganizationResponse;
import com.hng.stagetwo.response.OrganizationsResponse;
import com.hng.stagetwo.serviceImpl.OrganizationServiceImpl;
import com.hng.stagetwo.utils.InfoGetter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OrganizationServiceImplTests {
    @InjectMocks
    private OrganizationServiceImpl organizationService;
    @Mock
    private InfoGetter infoGetter;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private UserRepository userRepository;
    private User loggedInUser;
    private Organization organization;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock logged in user
        loggedInUser = new User();
        loggedInUser.setId(1L);
        loggedInUser.setEmail("testuser@example.com");

        // Mock organization
        organization = new Organization();
        organization.setOrgId("1");
        organization.setName("Test Organization");
        organization.setDescription("Description");
        organization.setUsers(Set.of(loggedInUser));
    }

    @Test
    void testGetUserOrganizations() {
        List<Organization> userOrganizations = new ArrayList<>();
        userOrganizations.add(organization);

        when(infoGetter.getEmailOfLoggedInUser()).thenReturn(loggedInUser.getEmail());
        when(infoGetter.getUser(loggedInUser.getEmail())).thenReturn(loggedInUser);
        when(infoGetter.findOrganizationsByUserId(loggedInUser.getId())).thenReturn(userOrganizations);

        ResponseEntity<?> response = organizationService.getUserOrganizations();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        OrganizationsResponse organizationsResponse = (OrganizationsResponse) response.getBody();
        assertNotNull(organizationsResponse);
        assertEquals("Success", organizationsResponse.getStatus());
        assertEquals("Organizations retrieved", organizationsResponse.getMessage());

        List<OrganizationResponseDto> organizationDtos = organizationsResponse.getData();
        assertNotNull(organizationDtos);
        assertEquals(1, organizationDtos.size());
        assertEquals(organization.getOrgId(), organizationDtos.get(0).getOrgId());
    }
    @Test
    void testGetOrganizationAccessAllowed() {
        when(infoGetter.getEmailOfLoggedInUser()).thenReturn(loggedInUser.getEmail());
        when(infoGetter.getUser(loggedInUser.getEmail())).thenReturn(loggedInUser);
        when(infoGetter.findByOrgId(organization.getOrgId())).thenReturn(organization);

        ResponseEntity<?> response = organizationService.getOrganization(organization.getOrgId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrganizationResponse organizationResponse = (OrganizationResponse) response.getBody();
        assertNotNull(organizationResponse);
        assertEquals("Success", organizationResponse.getStatus());
        assertEquals("Organization details retrieved", organizationResponse.getMessage());
        assertEquals(organization.getOrgId(), organizationResponse.getData().getOrgId());
    }
    @Test
    void testGetOrganizationAccessDenied() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail("otheruser@example.com");

        Organization otherOrganization = new Organization();
        otherOrganization.setOrgId("2");
        otherOrganization.setName("Other Organization");
        otherOrganization.setDescription("Description");
        organization.setUsers(Set.of(loggedInUser));;

        when(infoGetter.getEmailOfLoggedInUser()).thenReturn(loggedInUser.getEmail());
        when(infoGetter.getUser(loggedInUser.getEmail())).thenReturn(loggedInUser);
        when(infoGetter.findByOrgId(otherOrganization.getOrgId())).thenReturn(otherOrganization);

        ResponseEntity<?> response = organizationService.getOrganization(otherOrganization.getOrgId());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", ((ExceptionResponse<?>) response.getBody()).getStatus());
        assertEquals("Access denied", ((ExceptionResponse<?>) response.getBody()).getMessage());
    }
    @Test
    void testCreateOrganization() {
        OrganizationRequestDto requestDto = new OrganizationRequestDto();
        requestDto.setName("New Organization");
        requestDto.setDescription("Description");

        // Mocking the infoGetter methods
        when(infoGetter.getEmailOfLoggedInUser()).thenReturn(loggedInUser.getEmail());
        when(infoGetter.getUser(loggedInUser.getEmail())).thenReturn(loggedInUser);
        when(infoGetter.generateOrganizationId()).thenReturn("AM_12345");

        // Mocking the organizationRepository save method
        Organization savedOrganization = new Organization();
        savedOrganization.setOrgId("AM_12345");
        savedOrganization.setName(requestDto.getName());
        savedOrganization.setDescription(requestDto.getDescription());
        savedOrganization.addUser(loggedInUser);

        // Mocking the organizationRepository save method
        when(organizationRepository.save(any(Organization.class))).thenAnswer(invocation -> {
            Organization org = invocation.getArgument(0);
            org.setId(1L); // Simulate database assigning an ID
            return org;
        });

        // Mocking the userRepository findByEmail method
        when(userRepository.findByEmail(loggedInUser.getEmail())).thenReturn(Optional.of(loggedInUser));

        // Mocking the organizationRepository findByUsers_Id method
        when(organizationRepository.findByUsers_Id(loggedInUser.getId())).thenReturn(List.of(savedOrganization));

        // Perform the createOrganization request
        ResponseEntity<?> response = organizationService.createOrganization(requestDto);

        // Validate the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OrganizationResponse organizationResponse = (OrganizationResponse) response.getBody();
        assertNotNull(organizationResponse);
        assertEquals("Success", organizationResponse.getStatus());
        assertEquals("Organization created successfully", organizationResponse.getMessage());
        assertEquals("AM_12345", organizationResponse.getData().getOrgId());
        assertEquals(requestDto.getName(), organizationResponse.getData().getName());
        assertEquals(requestDto.getDescription(), organizationResponse.getData().getDescription());

        // Validate that the user and organization are linked correctly
        User user = userRepository.findByEmail(loggedInUser.getEmail()).orElse(null);
        assertNotNull(user);

        List<Organization> organizations = organizationRepository.findByUsers_Id(user.getId());
        assertNotNull(organizations);
        assertEquals(1, organizations.size());
        assertEquals("New Organization", organizations.get(0).getName());
    }
    @Test
    void testAddUserToOrganizationSuccess() {
        AddUserRequest request = new AddUserRequest();
        request.setUserId("2");

        User userToAdd = new User();
        userToAdd.setId(2L);
        userToAdd.setEmail("newuser@example.com");

        when(organizationRepository.findByOrgId(organization.getOrgId())).thenReturn(Optional.of(organization));
        when(userRepository.findByUserId(request.getUserId())).thenReturn(Optional.of(userToAdd));
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);
        when(userRepository.save(any(User.class))).thenReturn(userToAdd);

        ResponseEntity<?> response = organizationService.addUserToOrganization(organization.getOrgId(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AddUserToOrgResponse addUserResponse = (AddUserToOrgResponse) response.getBody();
        assertNotNull(addUserResponse);
        assertEquals("Success", addUserResponse.getStatus());
        assertEquals("User added to organization successfully", addUserResponse.getMessage());
    }
    @Test
    void testAddUserToOrganizationOrgNotFound() {
        AddUserRequest request = new AddUserRequest();
        request.setUserId("2");

        when(organizationRepository.findByOrgId(organization.getOrgId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> organizationService.addUserToOrganization(organization.getOrgId(), request));
    }
    @Test
    void testAddUserToOrganizationUserNotFound() {
        AddUserRequest request = new AddUserRequest();
        request.setUserId("2");

        when(organizationRepository.findByOrgId(organization.getOrgId())).thenReturn(Optional.of(organization));
        when(userRepository.findByUserId(request.getUserId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> organizationService.addUserToOrganization(organization.getOrgId(), request));
    }
}
