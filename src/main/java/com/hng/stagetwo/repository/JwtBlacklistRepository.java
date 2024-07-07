package com.hng.stagetwo.repository;

import com.hng.stagetwo.model.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    Optional<JwtBlacklist> findTokenBlacklistByToken(String token);
}