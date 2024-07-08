package com.hng.stagetwo;

import com.hng.stagetwo.jwt.service.JwtTokenService;
import com.hng.stagetwo.serviceImpl.UserDetailsServiceImpl;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtTokenServiceTests {
    @InjectMocks
    private JwtTokenService jwtTokenService;
    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    private HttpServletRequest request;
    private final String secretKey = "VGhpcyBpcyBhIHNlY3JldCBrZXkgZm9yIHRlc3Rpbmc=";
    private long validityInMilliseconds = 600000; // 10 minutes

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenService = new JwtTokenService();
        jwtTokenService.secretKey = secretKey;
        jwtTokenService.validityInMilliseconds = validityInMilliseconds;

        try {
            Field field = JwtTokenService.class.getDeclaredField("userDetailsServiceImpl");
            field.setAccessible(true);
            field.set(jwtTokenService, userDetailsServiceImpl);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateToken() {
        String email = "testuser@example.com";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtTokenService.createToken(email, roles);

        assertNotNull(token);
        assertEquals(email, jwtTokenService.getUsername(token));
        assertTrue(jwtTokenService.isTokenNotExpired(token));
    }
    @Test
    void testGetUsername() {
        String email = "testuser@example.com";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtTokenService.createToken(email, roles);

        assertEquals(email, jwtTokenService.getUsername(token));
    }
    @Test
    void testIsTokenNotExpired() {
        String email = "testuser@example.com";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtTokenService.createToken(email, roles);

        assertTrue(jwtTokenService.isTokenNotExpired(token));
    }
    @Test
    void testResolveToken() {
        String token = "Bearer abcdefghijklmnopqrstuvwxyz";
        when(request.getHeader("Authorization")).thenReturn(token);

        assertEquals("abcdefghijklmnopqrstuvwxyz", jwtTokenService.resolveToken(request));
    }
    @Test
    void testGetAuthentication() {
        String email = "testuser@example.com";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtTokenService.createToken(email, roles);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsServiceImpl.loadUserByUsername(email)).thenReturn(userDetails);

        Authentication authentication = jwtTokenService.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
    }
    private Key getSignKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
