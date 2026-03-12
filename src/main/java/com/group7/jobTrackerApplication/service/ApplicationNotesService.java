package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.DTO.CreateApplicationNoteRequest;
import com.group7.jobTrackerApplication.DTO.UpdateApplicationNoteRequest;
import com.group7.jobTrackerApplication.model.ApplicationNote;
import com.group7.jobTrackerApplication.model.JobApplication;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.repository.ApplicationNoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    public ApplicationNote getNoteById(Long noteId, Long applicationId, User user) {
        return applicationNoteRepository.findByNotesIdAndApplication_ApplicationIdAndApplication_User_UserId(noteId, applicationId, user.getUserId())
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    public ApplicationNote create(CreateApplicationNoteRequest request, User user) {

        ApplicationNote ap = new ApplicationNote();
        ap.setContent(request.content());
        ap.setLastEdited(request.lastEdited());
        ap.setApplicationId(request.jobApplication().getApplicationId());

        return applicationNoteRepository.save(ap);
    }

    public ApplicationNote patch(Long notesId, UpdateApplicationNoteRequest request, User user) {

        ApplicationNote toChange = applicationNoteRepository.findByNotesIdAndApplication_ApplicationIdAndApplication_User_UserId(notesId, request.application().getApplicationId(), user.getUserId())
                .orElseThrow(()-> new RuntimeException("Job Application not found"));

        if(request.lastEdited() != null) toChange.setLastEdited(request.lastEdited());
        if(request.content() != null) toChange.setContent(request.content());

        return applicationNoteRepository.save(toChange);
    }

    public void delete(Long noteId, Long applicationId,  User user) {
        ApplicationNote toDelete = applicationNoteRepository.findByNotesIdAndApplication_ApplicationIdAndApplication_User_UserId(noteId, applicationId, user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job application not found"));

        applicationNoteRepository.deleteById(toDelete.getNotesId());
    }
}