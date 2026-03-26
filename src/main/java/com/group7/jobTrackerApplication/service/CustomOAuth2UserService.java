package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.model.Role;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.HashSet;
import java.util.Set;

/**
 * Custom OAuth2 user service that extends the default Spring OAuth2 user service.
 * Handles loading and creating users from GitHub OAuth authentication.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final String adminGithubLogin;

    /**
     * Constructs a CustomOAuth2UserService with the given repository and admin login.
     *
     * @param userRepository the repository used to manage users
     * @param adminGithubLogin the GitHub login of the admin user
     */
    public CustomOAuth2UserService(
            UserRepository userRepository,
            @Value("${app.admin.github-login:}") String adminGithubLogin
    ) {
        this.userRepository = userRepository;
        this.adminGithubLogin = adminGithubLogin;
    }

    /**
     * Loads a user from GitHub OAuth and creates or updates their record in the database.
     *
     * @param request the OAuth2 user request containing the access token and client registration
     * @return the authenticated OAuth2 user with their granted authorities
     * @throws OAuth2AuthenticationException if the GitHub login or ID attribute is missing
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(request);

        String login = (String) oauthUser.getAttribute("login");
        if (login == null) {
            throw new OAuth2AuthenticationException("Missing GitHub login attribute");
        }

        Integer githubId = (Integer) oauthUser.getAttribute("id");
        if (githubId == null) {
            throw new OAuth2AuthenticationException("Missing GitHub id attribute");
        }
        String oauthSubject = githubId.toString();

        User user = userRepository.findByOauthProviderAndOauthSubject("github", oauthSubject)
                .map(existingUser -> {
                    Role desiredRole = resolveRole(login);
                    if (existingUser.getRole() != desiredRole) {
                        existingUser.setRole(desiredRole);
                        return userRepository.save(existingUser);
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    User u = new User();
                    u.setUsername(login);
                    u.setEmail((String) oauthUser.getAttribute("email"));
                    u.setOauthProvider("github");
                    u.setOauthSubject(oauthSubject);
                    u.setRole(resolveRole(login));
                    return userRepository.save(u);
                });

        Set<GrantedAuthority> authorities = new HashSet<>(oauthUser.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new DefaultOAuth2User(authorities, oauthUser.getAttributes(), "login");
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