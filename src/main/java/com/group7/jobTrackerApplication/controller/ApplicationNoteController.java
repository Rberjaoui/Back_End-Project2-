package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.model.ApplicationNote;
import com.group7.jobTrackerApplication.service.ApplicationNotesService;
import com.group7.jobTrackerApplication.DTO.UpdateApplicationNoteRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application-notes")
public class ApplicationNoteController {

    private final ApplicationNotesService applicationNotesService;

    public ApplicationNoteController(ApplicationNotesService applicationNotesService){
        this.applicationNotesService = applicationNotesService;
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<ApplicationNote> getNoteById(@PathVariable Long noteId){
        return ResponseEntity.ok(applicationNotesService.getNoteById(noteId));
    }

    @PostMapping
    public ResponseEntity<ApplicationNote> create(@AuthenticationPrincipal OAuth2User principal, @RequestBody ApplicationNote applicationNotes){
        ApplicationNote created = applicationNotesService.create(principal, applicationNotes);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{noteId}")
    public ResponseEntity<ApplicationNote> patch(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long noteId, @Valid @RequestBody UpdateApplicationNoteRequest request){
        ApplicationNote updated = applicationNotesService.patch(noteId, principal, request.getContent());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long noteId){
        applicationNotesService.delete(noteId, principal);
        return ResponseEntity.noContent().build();
    }
}