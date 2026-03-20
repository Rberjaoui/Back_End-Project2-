package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.DTO.CreateJobApplicationRequest;
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
 * REST controller for managing job applications.
 *
 * <p>This controller provides endpoints for retrieving, creating, replacing,
 * partially updating, and deleting job applications for the authenticated user.</p>
 *
 * @author Team 7
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/job-applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserService userService;

    /**
     * Constructs a {@code JobApplicationController} with required services.
     *
     * @param jobApplicationService service used for job application operations
     * @param userService service used to resolve the authenticated user
     */
    public JobApplicationController(JobApplicationService jobApplicationService, UserService userService){
        this.jobApplicationService = jobApplicationService;
        this.userService = userService;
    }

    /**
     * Retrieves all job applications belonging to the authenticated user.
     *
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing a list of {@link JobApplication} objects
     */
    @GetMapping
    public ResponseEntity<List<JobApplication>> getAll(@AuthenticationPrincipal OAuth2User principal){
        return ResponseEntity.ok(
                jobApplicationService.getAll(userService.getOrCreateFromOAuth(principal))
        );
    }

    /**
     * Retrieves a specific job application by its ID.
     *
     * <p>The authenticated user must own the requested job application.</p>
     *
     * @param applicationId the ID of the job application to retrieve
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing the requested {@link JobApplication}
     */
    @GetMapping("/{applicationId}")
    public ResponseEntity<JobApplication> getById(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
                jobApplicationService.getById(
                        applicationId,
                        userService.getOrCreateFromOAuth(principal)
                )
        );
    }

    /**
     * Creates a new job application for the authenticated user.
     *
     * <p>On success, this endpoint returns HTTP 201 Created along with the
     * newly created job application.</p>
     *
     * @param jobApplication the request payload containing job application data
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing the created {@link JobApplication}
     */
    @PostMapping
    public ResponseEntity<JobApplication> create(
            @RequestBody CreateJobApplicationRequest jobApplication,
            @AuthenticationPrincipal OAuth2User principal) {
        JobApplication created = jobApplicationService.create(
                jobApplication,
                userService.getOrCreateFromOAuth(principal)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Fully replaces an existing job application.
     *
     * <p>This endpoint performs a full update of the targeted job application.
     * The authenticated user must own the job application being replaced.</p>
     *
     * @param applicationId the ID of the job application to replace
     * @param jobApplication the request payload containing replacement data
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing the updated {@link JobApplication}
     */
    @PutMapping("/{applicationId}")
    public ResponseEntity<JobApplication> replace(
            @PathVariable Long applicationId,
            @RequestBody UpdateJobApplicationRequest jobApplication,
            @AuthenticationPrincipal OAuth2User principal) {
        JobApplication updated = jobApplicationService.replace(
                applicationId,
                jobApplication,
                userService.getOrCreateFromOAuth(principal)
        );
        return ResponseEntity.ok(updated);
    }

    /**
     * Partially updates an existing job application.
     *
     * <p>Only fields provided in the request body are updated. The authenticated
     * user must own the job application being modified.</p>
     *
     * @param applicationId the ID of the job application to update
     * @param request the request payload containing fields to patch
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing the patched {@link JobApplication}
     */
    @PatchMapping("/{applicationId}")
    public ResponseEntity<JobApplication> patch(
            @PathVariable Long applicationId,
            @RequestBody UpdateJobApplicationRequest request,
            @AuthenticationPrincipal OAuth2User principal) {
        JobApplication patched = jobApplicationService.patch(
                applicationId,
                request,
                userService.getOrCreateFromOAuth(principal)
        );
        return ResponseEntity.ok(patched);
    }

    /**
     * Deletes a job application by its ID.
     *
     * <p>The authenticated user must own the job application being deleted.
     * On success, this endpoint returns HTTP 204 No Content.</p>
     *
     * @param applicationId the ID of the job application to delete
     * @param principal the authenticated OAuth2 user
     * @return an empty {@link ResponseEntity} with HTTP 204 No Content
     */
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<JobApplication> delete(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal OAuth2User principal) {
        jobApplicationService.delete(
                applicationId,
                userService.getOrCreateFromOAuth(principal)
        );
        return ResponseEntity.noContent().build();
    }
}