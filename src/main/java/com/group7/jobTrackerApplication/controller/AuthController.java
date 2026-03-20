package com.group7.jobTrackerApplication.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication-related endpoints.
 *
 * <p>This controller provides endpoints for retrieving information about the
 * currently authenticated OAuth2 user.</p>
 *
 * @author Team 7
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
public class AuthController {

    /**
     * Returns information about the currently authenticated user.
     *
     * <p>The response includes the user's name, email, and all OAuth attributes
     * provided by the authentication provider.</p>
     *
     * @param user the authenticated OAuth2 user
     * @return a map containing user identity and attribute information
     */
    @GetMapping("/api/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User user) {
        return Map.of(
                "name", user.getAttribute("name"),
                "email", user.getAttribute("email"),
                "attributes", user.getAttributes()
        );
    }
}