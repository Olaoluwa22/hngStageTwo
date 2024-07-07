package com.hng.stagetwo.service;

import com.hng.stagetwo.dto.request.AddUserRequest;
import com.hng.stagetwo.dto.request.OrganizationRequestDto;
import com.hng.stagetwo.model.Organization;
import org.springframework.http.ResponseEntity;

public interface OrganizationService {
    Organization createOrganization(String userFirstName,String userId);
     ResponseEntity<?> getUserOrganizations();
     ResponseEntity<?> getOrganization(String orgId);
     ResponseEntity<?> createOrganization(OrganizationRequestDto requestDto);
     ResponseEntity<?> addUserToOrganization(String orgId, AddUserRequest request);
}
