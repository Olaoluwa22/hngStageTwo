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
    public ResponseEntity<?> getUserOrganisations(){
        return organizationService.getUserOrganizations();
    }
    @GetMapping("/{orgId}")
    public ResponseEntity<?> getOrganisation(@PathVariable String orgId){
        return organizationService.getOrganization(orgId);
    }
    @PostMapping
    public ResponseEntity<?> createOrganisation(@Valid @RequestBody OrganizationRequestDto requestDto){
        return organizationService.createOrganization(requestDto);
    }
    @PostMapping("/{orgId}/users")
    public ResponseEntity<?> addUserToOrganisation(@Valid @RequestBody AddUserRequest request, @PathVariable String orgId){
        return organizationService.addUserToOrganization(orgId, request);
    }
}