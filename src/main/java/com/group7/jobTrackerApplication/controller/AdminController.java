package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.model.admin;
import com.group7.jobTrackerApplication.service.adminService;
import com.group7.jobTrackerApplication.DTO.UpdateUserRoleRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public adminController(AdminService adminService){
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<Admin> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Admin> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Admin> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserRoleRequest request){
        Admin updated = adminService.update(userId, request.getRole());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Admin> deleteUser(@PathVariable Long userId){
        Admin deleted = adminService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
