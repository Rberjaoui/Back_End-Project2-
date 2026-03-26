package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.DTO.UpdateUserRoleRequest;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

/**
 * Controller for handling admin user management endpoints.
 * Provides endpoints for retrieving, updating, and deleting users.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a UserController with the given UserService.
     *
     * @param adminService the service used to manage users
     */
    public UserController(UserService adminService){
        this.userService = adminService;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user with the given ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    /**
     * Updates the role of a user.
     *
     * @param userId the ID of the user to update
     * @param request the request containing the new role
     * @return the updated user
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserRoleRequest request){
        User updated = userService.update(userId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @return a 204 no content response
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}