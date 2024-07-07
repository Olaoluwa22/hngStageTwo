package com.hng.stagetwo.jwt.filter;

import com.hng.stagetwo.jwt.service.JwtBlacklistService;
import com.hng.stagetwo.jwt.service.JwtTokenService;
import com.hng.stagetwo.utils.ConstantMessages;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private JwtBlacklistService jwtBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = jwtTokenService.resolveToken(request);
            if(token != null && jwtTokenService.isTokenNotExpired(token) && jwtBlacklistService.isTokenNotBlacklisted(token)){
                Authentication jwtServiceAuthentication = jwtTokenService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(jwtServiceAuthentication);
            }
            filterChain.doFilter(request, response);
        }catch(Exception exception){
            throw new BadRequestException(ConstantMessages.BAD_REQUEST.getMessage());
        }
    }
}
