package com.kanban.auth.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PermissionService {
    public boolean hasPermission(String name, String... authorities) {
        SecurityContext context = SecurityContextHolder.getContext();
        List<String> authorityList = Arrays.stream(authorities)
                .map(authority -> name + "_" + authority)
                .toList();

        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authorityList::contains);
    }
}