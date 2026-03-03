package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.model.ApplicationNote;
import com.group7.jobTrackerApplication.service.ApplicationNotesService;
import com.group7.jobTrackerApplication.DTO.UpdateApplicationNoteRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application-notes")
public class ApplicationNoteController {

    private final ApplicationNotesService applicationNotesService;

    public ApplicationNoteController(ApplicationNotesService applicationNotesService){
        this.applicationNotesService = applicationNotesService;
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<applicationNotes> getNoteById(@PathVariable Long noteId){
        return ResponseEntity.ok(applicationNotesService.getNoteById(noteId));
    }

    @PostMapping
    public ResponseEntity<applicationNotes> create(@RequestBody applicationNotes applicationNotes){
        ApplicationNote created = applicationNotesService.create(applicationNotes);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{noteId}")
    ResponseEntity<applicationNotes> patch(@PathVariable Long noteId, @Valid @RequestBody UpdateApplicationNoteRequest request){
        ApplicationNote updated = applicationNotesService.patch(noteId, request.getContent());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{noteId}")
    ResponseEntity<applicationNotes> delete(@PathVariable Long noteId){
        ApplicationNote deleted = applicationNotesService.delete(noteId);
        return ResponseEntity.noContent().build();
    }

}
