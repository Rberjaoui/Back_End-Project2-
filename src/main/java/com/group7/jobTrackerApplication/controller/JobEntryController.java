package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.DTO.CreateJobEntryRequest;
import com.group7.jobTrackerApplication.DTO.GetJobEntryRequest;
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
 * Controller for handling job entry endpoints.
 * Provides endpoints for creating, retrieving, updating, and deleting job entries.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/job-entries")
public class JobEntryController {

    private final JobEntryService jobEntryService;
    private final UserService userService;

    /**
     * Constructs a JobEntryController with the given services.
     *
     * @param jobEntryService the service used to manage job entries
     * @param userService the service used to manage users
     */
    public JobEntryController(JobEntryService jobEntryService, UserService userService){
        this.jobEntryService = jobEntryService;
        this.userService = userService;
    }

    /**
     * Returns the authenticated user's OAuth attributes.
     *
     * @param user the currently authenticated OAuth2 user
     * @return the user's attributes or a not authenticated message
     */
    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal OAuth2User user) {
        return user == null ? "NOT AUTHENTICATED" : user.getAttributes();
    }

    /**
     * Retrieves all job entries for the authenticated user.
     *
     * @param principal the currently authenticated OAuth2 user
     * @return a list of job entries belonging to the authenticated user
     */
    @GetMapping
    public ResponseEntity<List<GetJobEntryRequest>> getAll(@AuthenticationPrincipal OAuth2User principal){
        return ResponseEntity.ok(jobEntryService.getAll(userService.getOrCreateFromOAuth(principal)));
    }

    /**
     * Retrieves a job entry by its ID.
     *
     * @param jobId the ID of the job entry to retrieve
     * @param principal the currently authenticated OAuth2 user
     * @return the job entry with the given ID
     */
    @GetMapping("/{jobId}")
    public ResponseEntity<GetJobEntryRequest> getById(@PathVariable Long jobId, @AuthenticationPrincipal OAuth2User principal){
        User user = userService.getOrCreateFromOAuth(principal);
        return ResponseEntity.ok(jobEntryService.getById(jobId, user));
    }

    /**
     * Creates a new job entry for the authenticated user.
     *
     * @param principal the currently authenticated OAuth2 user
     * @param request the request containing the job entry details
     * @return the created job entry with a 201 status
     */
    @PostMapping
    public ResponseEntity<JobEntry> create(@AuthenticationPrincipal OAuth2User principal, @RequestBody CreateJobEntryRequest request){
        JobEntry created = jobEntryService.create(principal, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Replaces a job entry with new data.
     *
     * @param jobId the ID of the job entry to replace
     * @param request the request containing the updated job entry details
     * @param principal the currently authenticated OAuth2 user
     * @return a 204 no content response
     */
    @PutMapping("/{jobId}")
    public ResponseEntity<JobEntry> replace(@PathVariable Long jobId, @RequestBody UpdateJobEntryRequest request, @AuthenticationPrincipal OAuth2User principal){
        JobEntry updated = jobEntryService.replace(jobId, request, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.noContent().build();
    }

    /**
     * Partially updates a job entry.
     *
     * @param jobId the ID of the job entry to update
     * @param request the request containing the fields to update
     * @param principal the currently authenticated OAuth2 user
     * @return a 204 no content response
     */
    @PatchMapping("/{jobId}")
    public ResponseEntity<JobEntry> patch(@PathVariable Long jobId, @RequestBody UpdateJobEntryRequest request, @AuthenticationPrincipal OAuth2User principal){
        JobEntry patched = jobEntryService.patch(jobId, request, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a job entry by its ID.
     *
     * @param jobId the ID of the job entry to delete
     * @param principal the currently authenticated OAuth2 user
     * @return a 204 no content response
     */
    @DeleteMapping("/{jobId}")
    public ResponseEntity<JobEntry> delete(@PathVariable Long jobId, @AuthenticationPrincipal OAuth2User principal){
        jobEntryService.delete(jobId, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.noContent().build();
    }
}