package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.DTO.CreateJobEntryRequest;
import com.group7.jobTrackerApplication.DTO.UpdateJobEntryRequest;
import com.group7.jobTrackerApplication.model.JobEntry;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.service.JobEntryService;
import com.group7.jobTrackerApplication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * REST controller for managing job entries.
 *
 * <p>This controller provides endpoints for viewing authentication details,
 * retrieving job entries, creating new entries, replacing existing entries,
 * partially updating entries, and deleting entries for the authenticated user.</p>
 *
 * @author Team 7
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/job-entries")
public class JobEntryController {

    private final JobEntryService jobEntryService;
    private final UserService userService;

    /**
     * Constructs a {@code JobEntryController} with required services.
     *
     * @param jobEntryService service used for job entry operations
     * @param userService service used to resolve the authenticated user
     */
    public JobEntryController(JobEntryService jobEntryService, UserService userService){
        this.jobEntryService = jobEntryService;
        this.userService = userService;
    }

    /**
     * Returns authentication details for the current OAuth2 user.
     *
     * <p>If no user is authenticated, this endpoint returns the string
     * {@code "NOT AUTHENTICATED"}.</p>
     *
     * @param user the authenticated OAuth2 user
     * @return the authenticated user's attribute map, or a not-authenticated message
     */
    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal OAuth2User user) {
        return user == null ? "NOT AUTHENTICATED" : user.getAttributes();
    }

    /**
     * Retrieves all job entries belonging to the authenticated user.
     *
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing a list of {@link JobEntry} objects
     */
    @GetMapping
    public ResponseEntity<List<JobEntry>> getAll(@AuthenticationPrincipal OAuth2User principal){
        return ResponseEntity.ok(
                jobEntryService.getAll(userService.getOrCreateFromOAuth(principal))
        );
    }

    /**
     * Retrieves a single job entry by its ID.
     *
     * <p>The authenticated user must own the requested job entry.</p>
     *
     * @param jobId the ID of the job entry to retrieve
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing the requested {@link JobEntry}
     */
    @GetMapping("/{jobId}")
    public ResponseEntity<JobEntry> getById(
            @PathVariable Long jobId,
            @AuthenticationPrincipal OAuth2User principal) {
        User user = userService.getOrCreateFromOAuth(principal);
        return ResponseEntity.ok(jobEntryService.getById(jobId, user));
    }

    /**
     * Creates a new job entry for the authenticated user.
     *
     * <p>On success, this endpoint returns HTTP 201 Created and the created
     * {@link JobEntry} object.</p>
     *
     * @param principal the authenticated OAuth2 user
     * @param request the request payload containing job entry data
     * @return a {@link ResponseEntity} containing the created {@link JobEntry}
     */
    @PostMapping
    public ResponseEntity<JobEntry> create(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody CreateJobEntryRequest request) {
        JobEntry created = jobEntryService.create(principal, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Fully replaces an existing job entry.
     *
     * <p>The authenticated user must own the job entry being updated.</p>
     *
     * @param jobId the ID of the job entry to replace
     * @param request the request payload containing replacement data
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing the updated {@link JobEntry}
     */
    @PutMapping("/{jobId}")
    public ResponseEntity<JobEntry> replace(
            @PathVariable Long jobId,
            @RequestBody UpdateJobEntryRequest request,
            @AuthenticationPrincipal OAuth2User principal) {
        JobEntry updated = jobEntryService.replace(
                jobId,
                request,
                userService.getOrCreateFromOAuth(principal)
        );
        return ResponseEntity.ok(updated);
    }

    /**
     * Partially updates an existing job entry.
     *
     * <p>Only fields included in the request body are updated.</p>
     *
     * @param jobId the ID of the job entry to patch
     * @param request the request payload containing updated fields
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing the patched {@link JobEntry}
     */
    @PatchMapping("/{jobId}")
    public ResponseEntity<JobEntry> patch(
            @PathVariable Long jobId,
            @RequestBody UpdateJobEntryRequest request,
            @AuthenticationPrincipal OAuth2User principal) {
        JobEntry patched = jobEntryService.patch(
                jobId,
                request,
                userService.getOrCreateFromOAuth(principal)
        );
        return ResponseEntity.ok(patched);
    }

    /**
     * Deletes a job entry by its ID.
     *
     * <p>The authenticated user must own the job entry being deleted.
     * On success, this endpoint returns HTTP 204 No Content.</p>
     *
     * @param jobId the ID of the job entry to delete
     * @param principal the authenticated OAuth2 user
     * @return an empty {@link ResponseEntity} with HTTP 204 No Content
     */
    @DeleteMapping("/{jobId}")
    public ResponseEntity<JobEntry> delete(
            @PathVariable Long jobId,
            @AuthenticationPrincipal OAuth2User principal) {
        jobEntryService.delete(jobId, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.noContent().build();
    }
}