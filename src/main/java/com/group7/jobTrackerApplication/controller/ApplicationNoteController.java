package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.DTO.CreateApplicationNoteRequest;
import com.group7.jobTrackerApplication.DTO.UpdateApplicationNoteRequest;
import com.group7.jobTrackerApplication.model.ApplicationNote;
import com.group7.jobTrackerApplication.service.ApplicationNotesService;
import com.group7.jobTrackerApplication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing notes associated with job applications.
 *
 * <p>This controller provides endpoints for retrieving, creating, updating,
 * and deleting notes tied to a specific job application. All note operations
 * are performed in the context of the authenticated OAuth user.</p>
 *
 * @author Team 7
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/job-applications/{applicationId}/note")
public class ApplicationNoteController {

    private final ApplicationNotesService applicationNotesService;
    private final UserService userService;

    /**
     * Constructs an {@code ApplicationNoteController} with required services.
     *
     * @param applicationNotesService service used to manage application note operations
     * @param userService service used to resolve the authenticated user
     */
    public ApplicationNoteController(ApplicationNotesService applicationNotesService, UserService userService){
        this.applicationNotesService = applicationNotesService;
        this.userService = userService;
    }

    /**
     * Retrieves a single application note by its ID for a specific job application.
     *
     * <p>The authenticated user must own the related job application and note.</p>
     *
     * @param principal the authenticated OAuth2 user
     * @param noteId the ID of the note to retrieve
     * @param applicationId the ID of the parent job application
     * @return a {@link ResponseEntity} containing the requested {@link ApplicationNote}
     */
    @GetMapping("/{noteId}")
    public ResponseEntity<ApplicationNote> getNoteById(
            @AuthenticationPrincipal OAuth2User principal,
            @PathVariable Long noteId,
            @PathVariable Long applicationId) {
        return ResponseEntity.ok(
                applicationNotesService.getNoteById(
                        noteId,
                        applicationId,
                        userService.getOrCreateFromOAuth(principal)
                )
        );
    }

    /**
     * Creates a new application note for the authenticated user.
     *
     * <p>This endpoint creates a note using the request body data and returns
     * the newly created note with HTTP 201 Created.</p>
     *
     * @param principal the authenticated OAuth2 user
     * @param request the request payload containing new note data
     * @return a {@link ResponseEntity} containing the created {@link ApplicationNote}
     */
    @PostMapping
    public ResponseEntity<ApplicationNote> create(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody CreateApplicationNoteRequest request) {
        ApplicationNote created = applicationNotesService.create(
                request,
                userService.getOrCreateFromOAuth(principal)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Partially updates an existing application note.
     *
     * <p>Only fields provided in the request body are updated. The authenticated
     * user must own the note being modified.</p>
     *
     * @param noteId the ID of the note to update
     * @param request the request payload containing fields to update
     * @param principal the authenticated OAuth2 user
     * @return a {@link ResponseEntity} containing the updated {@link ApplicationNote}
     */
    @PatchMapping("/{noteId}")
    public ResponseEntity<ApplicationNote> patch(
            @PathVariable Long noteId,
            @Valid @RequestBody UpdateApplicationNoteRequest request,
            @AuthenticationPrincipal OAuth2User principal) {
        ApplicationNote updated = applicationNotesService.patch(
                noteId,
                request,
                userService.getOrCreateFromOAuth(principal)
        );
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes an application note by its ID.
     *
     * <p>The authenticated user must own the parent job application and note.
     * On success, this endpoint returns HTTP 204 No Content.</p>
     *
     * @param noteId the ID of the note to delete
     * @param applicationId the ID of the parent job application
     * @param principal the authenticated OAuth2 user
     * @return an empty {@link ResponseEntity} with HTTP 204 No Content
     */
    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApplicationNote> delete(
            @PathVariable Long noteId,
            @PathVariable Long applicationId,
            @AuthenticationPrincipal OAuth2User principal) {
        applicationNotesService.delete(
                noteId,
                applicationId,
                userService.getOrCreateFromOAuth(principal)
        );
        return ResponseEntity.noContent().build();
    }
}