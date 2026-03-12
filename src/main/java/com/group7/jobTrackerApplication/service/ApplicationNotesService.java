package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.model.ApplicationNote;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.repository.ApplicationNoteRepository;
import com.group7.jobTrackerApplication.exception.ResourceNotFoundException;
import com.group7.jobTrackerApplication.exception.ForbiddenException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class ApplicationNotesService {

    private final ApplicationNoteRepository applicationNoteRepository;
    private final UserService userService;

    public ApplicationNotesService(ApplicationNoteRepository applicationNoteRepository, UserService userService) {
        this.applicationNoteRepository = applicationNoteRepository;
        this.userService = userService;
    }

    public ApplicationNote getNoteById(Long noteId) {
        return applicationNoteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + noteId));
    }

    public ApplicationNote create(OAuth2User principal, ApplicationNote applicationNote) {
        User user = userService.getOrCreateFromOAuth(principal);
        applicationNote.setUserId(user.getUserId());
        return applicationNoteRepository.save(applicationNote);
    }

    public ApplicationNote patch(Long noteId, OAuth2User principal, String newContent) {
        ApplicationNote note = getNoteById(noteId);
        User user = userService.getOrCreateFromOAuth(principal);

        if (!note.getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("You do not have permission to update this note");
        }

        note.setContent(newContent);
        return applicationNoteRepository.save(note);
    }

    public ApplicationNote delete(Long noteId, OAuth2User principal) {
        ApplicationNote note = getNoteById(noteId);
        User user = userService.getOrCreateFromOAuth(principal);

        if (!note.getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("You do not have permission to delete this note");
        }

        applicationNoteRepository.deleteById(noteId);
        return note;
    }
}