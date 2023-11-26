package com.kanban.auth.service;

import com.kanban.auth.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(
        cacheManager = "refreshCacheManager",
        cacheNames = "refreshKey"
)
public class AuthCacheService {

    private final JwtUtils jwtUtils;

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
}
