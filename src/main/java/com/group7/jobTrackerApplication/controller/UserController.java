package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.DTO.UpdateUserRoleRequest;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

/**
 * REST controller for administrative user management operations.
 *
 * <p>This controller provides admin endpoints for retrieving all users,
 * retrieving a user by ID, updating a user's role or other allowed fields,
 * and deleting users from the system.</p>
 *
 * @author Team 7
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a {@code UserController} with the required user service.
     *
     * @param adminService service used for administrative user operations
     */
    public UserController(UserService adminService){
        this.userService = adminService;
    }

    /**
     * Retrieves all users in the system.
     *
     * <p>This endpoint is intended for administrative use.</p>
     *
     * @return a {@link ResponseEntity} containing a list of all {@link User} objects
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a single user by ID.
     *
     * <p>This endpoint is intended for administrative use.</p>
     *
     * @param userId the ID of the user to retrieve
     * @return a {@link ResponseEntity} containing the requested {@link User}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    /**
     * Partially updates a user's data.
     *
     * <p>This endpoint is intended for administrative use and is commonly used
     * for updating a user's role.</p>
     *
     * @param userId the ID of the user to update
     * @param request the request payload containing updated user role data
     * @return a {@link ResponseEntity} containing the updated {@link User}
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleRequest request) {
        User updated = userService.update(userId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a user by ID.
     *
     * <p>This endpoint is intended for administrative use.
     * On success, it returns HTTP 204 No Content.</p>
     *
     * @param userId the ID of the user to delete
     * @return an empty {@link ResponseEntity} with HTTP 204 No Content
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}