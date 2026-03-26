package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.DTO.UpdateUserRoleRequest;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.model.Role;
import com.group7.jobTrackerApplication.repository.UserRepository;
import com.group7.jobTrackerApplication.exception.ResourceNotFoundException;
import com.group7.jobTrackerApplication.exception.NotAuthenticatedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing users.
 * Handles business logic for retrieving, updating, deleting, and authenticating users via OAuth.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final String adminGithubLogin;

    /**
     * Constructs a UserService with the given repository and admin login.
     *
     * @param userRepository the repository used to manage users
     * @param adminGithubLogin the GitHub login of the admin user
     */
    public UserService(
            UserRepository userRepository,
            @Value("${app.admin.github-login:}") String adminGithubLogin
    ) {
        this.userRepository = userRepository;
        this.adminGithubLogin = adminGithubLogin;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user with the given ID
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    /**
     * Updates the role of a user.
     *
     * @param userId the ID of the user to update
     * @param request the request containing the new role
     * @return the updated user
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    public User update(Long userId, UpdateUserRoleRequest request) {
        User user = getUserById(userId);
        user.setRole(request.role());
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @return the deleted user
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    public User delete(Long userId) {
        User user = getUserById(userId);
        userRepository.deleteById(userId);
        return user;
    }

    /**
     * Retrieves or creates a user from an OAuth2 principal.
     * If the user already exists, their role is updated if necessary.
     * If the user does not exist, a new user is created.
     *
     * @param principal the currently authenticated OAuth2 user
     * @return the existing or newly created user
     * @throws NotAuthenticatedException if the principal is null or the token is invalid
     */
    public User getOrCreateFromOAuth(OAuth2User principal) {
        if (principal == null) {
            throw new NotAuthenticatedException("Authentication required");
        }

        Map<String, Object> attrs = principal.getAttributes();

        if (attrs.get("id") == null) {
            throw new NotAuthenticatedException("Token expired or invalid");
        }

        String provider = "github";
        String subject = attrs.get("id").toString();
        String username = (String) attrs.get("login");
        String email = (String) attrs.get("email");

        return userRepository
                .findByOauthProviderAndOauthSubject(provider, subject)
                .map(existingUser -> {
                    Role desiredRole = resolveRole(username);
                    if (existingUser.getRole() != desiredRole) {
                        existingUser.setRole(desiredRole);
                        return userRepository.save(existingUser);
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    User u = new User();
                    u.setOauthProvider(provider);
                    u.setOauthSubject(subject);
                    u.setUsername(username);
                    u.setEmail(email);
                    u.setRole(resolveRole(username));
                    return userRepository.save(u);
                });
    }

    /**
     * Resolves the role of a user based on their GitHub login.
     *
     * @param githubLogin the GitHub login of the user
     * @return ADMIN if the login matches the configured admin login, USER otherwise
     */
    private Role resolveRole(String githubLogin) {
        if (StringUtils.hasText(adminGithubLogin) && adminGithubLogin.equalsIgnoreCase(githubLogin)) {
            return Role.ADMIN;
        }
        return Role.USER;
    }
}