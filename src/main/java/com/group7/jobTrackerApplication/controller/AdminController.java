package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.model.admin;
import com.group7.jobTrackerApplication.service.adminService;
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

    @GetMapping
}
