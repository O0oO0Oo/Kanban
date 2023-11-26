package com.kanban.auth.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PermissionService {
    public boolean hasPermission(String name, String... authorities) {
        List<String> authorityList = Arrays.stream(authorities)
                .map(authority -> name + authority)
                .toList();

        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(authorityList::contains);
    }
}
