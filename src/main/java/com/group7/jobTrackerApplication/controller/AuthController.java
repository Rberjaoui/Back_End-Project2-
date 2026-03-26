package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.service.UserService;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related endpoints.
 * Provides access to the currently authenticated user's information.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
public class AuthController {

    private final UserService userService;

    /**
     * Constructs an AuthController with the given UserService.
     *
     * @param userService the service used to manage users
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns information about the currently authenticated user.
     *
     * @param user the currently authenticated OAuth2 user
     * @return a map containing the user's name, login, email, role, authorities, and attributes
     */
    @GetMapping("/api/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User user) {
        User dbUser = userService.getOrCreateFromOAuth(user);

        return Map.of(
                "name", user.getAttribute("name"),
                "login", user.getAttribute("login"),
                "email", user.getAttribute("email"),
                "role", dbUser.getRole().name(),
                "authorities", user.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .sorted()
                        .collect(Collectors.toList()),
                "attributes", user.getAttributes()
        );
    }
}