package com.hng.stagetwo.repository;

import com.hng.stagetwo.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByUsers_Id(Long userId);
    Optional<Organization> findByOrgId(String orgId);
}