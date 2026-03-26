package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.DTO.CreateApplicationNoteRequest;
import com.group7.jobTrackerApplication.DTO.GetApplicationNoteSummary;
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
 * Controller for handling application note endpoints.
 * Provides endpoints for creating, retrieving, updating, and deleting notes on job applications.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/job-applications/{applicationId}/note")
public class ApplicationNoteController {

    private final ApplicationNotesService applicationNotesService;
    private final UserService userService;

    /**
     * Constructs an ApplicationNoteController with the given services.
     *
     * @param applicationNotesService the service used to manage application notes
     * @param userService the service used to manage users
     */
    public ApplicationNoteController(ApplicationNotesService applicationNotesService, UserService userService){
        this.applicationNotesService = applicationNotesService;
        this.userService = userService;
    }

    /**
     * Retrieves a note by its ID.
     *
     * @param principal the currently authenticated OAuth2 user
     * @param noteId the ID of the note to retrieve
     * @param applicationId the ID of the job application the note belongs to
     * @return the note with the given ID
     */
    @GetMapping("/{noteId}")
    public ResponseEntity<GetApplicationNoteSummary> getNoteById(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long noteId, @PathVariable Long applicationId){
        ApplicationNote note = applicationNotesService.getNoteById(noteId, applicationId, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.ok(toNoteSummary(note));
    }

    /**
     * Creates a new note for a job application.
     *
     * @param principal the currently authenticated OAuth2 user
     * @param request the request containing the note details
     * @return the created note with a 201 status
     */
    @PostMapping
    public ResponseEntity<GetApplicationNoteSummary> create(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody CreateApplicationNoteRequest request
    ){
        ApplicationNote created = applicationNotesService.create(request.jobApplication().getApplicationId(), request, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(toNoteSummary(created));
    }

    /**
     * Partially updates a note.
     *
     * @param noteId the ID of the note to update
     * @param request the request containing the fields to update
     * @param principal the currently authenticated OAuth2 user
     * @return the updated note
     */
    @PatchMapping("/{noteId}")
    public ResponseEntity<GetApplicationNoteSummary> patch(@PathVariable Long noteId, @Valid @RequestBody UpdateApplicationNoteRequest request, @AuthenticationPrincipal OAuth2User principal){
        ApplicationNote updated = applicationNotesService.patch(request.application().getApplicationId(), noteId, request, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.ok(toNoteSummary(updated));
    }

    /**
     * Deletes a note by its ID.
     *
     * @param noteId the ID of the note to delete
     * @param applicationId the ID of the job application the note belongs to
     * @param principal the currently authenticated OAuth2 user
     * @return a 204 no content response
     */
    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApplicationNote> delete(@PathVariable Long noteId, @PathVariable Long applicationId, @AuthenticationPrincipal OAuth2User principal){
        applicationNotesService.delete(noteId, applicationId, userService.getOrCreateFromOAuth(principal));
        return ResponseEntity.noContent().build();
    }

    /**
     * Converts an ApplicationNote to a GetApplicationNoteSummary DTO.
     *
     * @param note the note to convert
     * @return the note summary DTO
     */
    private GetApplicationNoteSummary toNoteSummary(ApplicationNote note) {
        return new GetApplicationNoteSummary(
                note.getNotesId(),
                note.getApplicationId(),
                note.getApplication().getJobEntry().getJobTitle(),
                note.getApplication().getJobEntry().getCompanyName(),
                note.getApplication().getStatus(),
                note.getLastEdited(),
                note.getContent()
        );
    }
}