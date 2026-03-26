package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.DTO.CreateJobApplicationRequest;
import com.group7.jobTrackerApplication.DTO.GetJobApplicationRequest;
import com.group7.jobTrackerApplication.DTO.UpdateJobApplicationRequest;
import com.group7.jobTrackerApplication.model.JobApplication;
import com.group7.jobTrackerApplication.service.JobApplicationService;
import com.group7.jobTrackerApplication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for handling job application endpoints.
 * Provides endpoints for creating, retrieving, updating, and deleting job applications.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/job-applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserService userService;

    /**
     * Constructs a JobApplicationController with the given services.
     *
     * @param jobApplicationService the service used to manage job applications
     * @param userService the service used to manage users
     */
    public JobApplicationController(JobApplicationService jobApplicationService, UserService userService){
        this.jobApplicationService = jobApplicationService;
        this.userService = userService;
    }

    /**
     * Retrieves all job applications for the authenticated user.
     *
     * @param principal the currently authenticated OAuth2 user
     * @return a list of job applications belonging to the authenticated user
     */
    @GetMapping
    public ResponseEntity<List<GetJobApplicationRequest>> getAll(@AuthenticationPrincipal OAuth2User principal){
        return ResponseEntity.ok(jobApplicationService.getAll(userService.getOrCreateFromOAuth(principal)));
    }

    /**
     * Retrieves a job application by its ID.
     *
     * @param applicationId the ID of the job application to retrieve
     * @param principal the currently authenticated OAuth2 user
     * @return the job application with the given ID
     */
    @GetMapping("/{applicationId}")
    public ResponseEntity<GetJobApplicationRequest> getById(@PathVariable Long applicationId, @AuthenticationPrincipal OAuth2User principal){
        return ResponseEntity.ok(jobApplicationService.getById(applicationId, userService.getOrCreateFromOAuth(principal)));
    }

    /**
     * Creates a new job application for the authenticated user.
     *
     * @param jobApplication the request containing the job application details
     * @param principal the currently authenticated OAuth2 user
     * @return the created job application with a 201 status
     */
    @PostMapping
    public ResponseEntity<JobApplication> create(@RequestBody CreateJobApplicationRequest jobApplication, @AuthenticationPrincipal OAuth2User principal){
        JobApplication created = jobApplicationService.create(jobApplication, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Replaces a job application with new data.
     *
     * @param applicationId the ID of the job application to replace
     * @param jobApplication the request containing the updated job application details
     * @param principal the currently authenticated OAuth2 user
     * @return a 204 no content response
     */
    @PutMapping("/{applicationId}")
    public ResponseEntity<JobApplication> replace(@PathVariable Long applicationId, @RequestBody UpdateJobApplicationRequest jobApplication, @AuthenticationPrincipal OAuth2User principal){
        JobApplication updated = jobApplicationService.replace(applicationId, jobApplication, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.noContent().build();
    }

    /**
     * Partially updates a job application.
     *
     * @param applicationId the ID of the job application to update
     * @param request the request containing the fields to update
     * @param principal the currently authenticated OAuth2 user
     * @return a 204 no content response
     */
    @PatchMapping("/{applicationId}")
    public ResponseEntity<JobApplication> patch(@PathVariable Long applicationId, @RequestBody UpdateJobApplicationRequest request, @AuthenticationPrincipal OAuth2User principal){
        JobApplication patched = jobApplicationService.patch(applicationId, request, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a job application by its ID.
     *
     * @param applicationId the ID of the job application to delete
     * @param principal the currently authenticated OAuth2 user
     * @return a 204 no content response
     */
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<JobApplication> delete(@PathVariable Long applicationId, @AuthenticationPrincipal OAuth2User principal){
        jobApplicationService.delete(applicationId, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.noContent().build();
    }
}