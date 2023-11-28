package com.kanban.auth.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@TestConfiguration
@EnableMethodSecurity(securedEnabled = true)
public class TestSecurityConfig {
}
