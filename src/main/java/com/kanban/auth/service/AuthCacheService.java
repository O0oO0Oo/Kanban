package com.kanban.auth.service;

import com.kanban.auth.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@CacheConfig(
        cacheManager = "refreshCacheManager",
        cacheNames = "refreshKey"
)
public class AuthCacheService {

    private final AuthCacheService self;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthCacheService(@Lazy AuthCacheService self, JwtUtils jwtUtils) {
        this.self = self;
        this.jwtUtils = jwtUtils;
    }

    @CachePut(key = "#authentication.getName()")
    public String addRefreshKey(Authentication authentication) {
        log.info("user : {} Refresh Key added.", authentication.getName());
        return jwtUtils.generateRefreshToken(authentication);
    }

    @Cacheable(
            key = "#authentication.getName()",
            unless = "#result.isEmpty()")
    public String findRefreshKey(Authentication authentication) {
        log.info("find user : {} Refresh Key.", authentication.getName());
        return "";
    }

    @CachePut(key = "#authentication.getName() + ':authority'")
    public String addAuthorities(Authentication authentication) {
        log.info("user : {} Authorities added", authentication.getName());
        return authoritiesToString(authentication.getAuthorities());
    }

    @Cacheable(
            key = "#principal + ':authority'",
            unless = "#result.isEmpty()")
    public String findAuthorities(String principal) {
        log.info("find user : {} Authorities", principal);
        return "";
    }

    @CachePut(key = "#principal + ':authority'")
    public String updateAuthorities(String principal, String newAuthority) {
        log.info("update user : {} Authorities updated");
        String authorities = self.findAuthorities(principal);
        return authorities + "," + newAuthority;
    }

    private String authoritiesToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
