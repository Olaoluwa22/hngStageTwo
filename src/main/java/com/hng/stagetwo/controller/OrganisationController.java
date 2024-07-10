package com.hng.stagetwo.controller;

import com.hng.stagetwo.dto.request.AddUserRequest;
import com.hng.stagetwo.dto.request.OrganizationRequestDto;
import com.hng.stagetwo.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organisations")
public class OrganisationController {
    @Autowired
    private OrganizationService organizationService;
    @GetMapping
    public ResponseEntity<?> getUserOrganizations(){
        return organizationService.getUserOrganizations();
    }
    @GetMapping("/{orgId}")
    public ResponseEntity<?> getOrganization(@PathVariable String orgId){
        return organizationService.getOrganization(orgId);
    }
    @PostMapping
    public ResponseEntity<?> createOrganization(@Valid @RequestBody OrganizationRequestDto requestDto){
        return organizationService.createOrganization(requestDto);
    }
    @PostMapping("/{orgId}/users")
    public ResponseEntity<?> addUserToOrganization(@Valid @RequestBody AddUserRequest request, @PathVariable String orgId){
        return organizationService.addUserToOrganization(orgId, request);
    }
}