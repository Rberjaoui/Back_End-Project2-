package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.DTO.ApplicationNoteRequest;
import com.group7.jobTrackerApplication.DTO.CreateApplicationNoteRequest;
import com.group7.jobTrackerApplication.DTO.GetApplicationNoteSummary;
import com.group7.jobTrackerApplication.DTO.UpdateApplicationNoteRequest;
import com.group7.jobTrackerApplication.model.JobApplication;
import com.group7.jobTrackerApplication.model.ApplicationNote;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.repository.JobApplicationRepository;
import com.group7.jobTrackerApplication.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Service;
import com.group7.jobTrackerApplication.exception.ResourceNotFoundException;
import com.group7.jobTrackerApplication.exception.ForbiddenException;
import java.util.List;

/**
 * Service class for managing application notes.
 * Handles business logic for creating, retrieving, updating, and deleting notes on job applications.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@Service
public class ApplicationNotesService {

    private final ApplicationNoteRepository applicationNoteRepository;
    private final JobApplicationRepository jobApplicationRepository;

    /**
     * Constructs an ApplicationNotesService with the given repositories.
     *
     * @param applicationNoteRepository the repository used to manage application notes
     * @param jobApplicationRepository the repository used to manage job applications
     */
    public ApplicationNotesService(
            ApplicationNoteRepository applicationNoteRepository,
            JobApplicationRepository jobApplicationRepository
    ) {
        this.applicationNoteRepository = applicationNoteRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    /**
     * Retrieves a note by its ID, application ID, and the authenticated user.
     *
     * @param noteId the ID of the note to retrieve
     * @param applicationId the ID of the job application the note belongs to
     * @param user the authenticated user requesting the note
     * @return the note matching the given IDs and user
     * @throws ResourceNotFoundException if no matching note is found
     */
    public ApplicationNote getNoteById(Long noteId, Long applicationId, User user) {
        return applicationNoteRepository.findByNotesIdAndApplication_ApplicationIdAndApplication_User_UserId(noteId, applicationId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Application Note not found"));
    }

    /**
     * Creates a new note for a job application, or updates the existing note if one already exists.
     *
     * @param applicationId the ID of the job application to create the note for
     * @param request the request containing the note details
     * @param user the authenticated user creating the note
     * @return the created or updated note
     * @throws ForbiddenException if the user does not own the job application
     */
    public ApplicationNote create(Long applicationId, CreateApplicationNoteRequest request, User user) {
        JobApplication jobApplication = jobApplicationRepository
                .findByApplicationIdAndUser_UserId(applicationId, user.getUserId())
                .orElseThrow(() -> new ForbiddenException("Not authorized to create this note"));

        ApplicationNote existing = applicationNoteRepository
                .findByApplication_ApplicationIdAndApplication_User_UserId(applicationId, user.getUserId())
                .orElse(null);

        if (existing != null) {
            existing.setContent(request.content());
            existing.setLastEdited(request.lastEdited());
            return applicationNoteRepository.save(existing);
        }

        ApplicationNote ap = new ApplicationNote();
        ap.setContent(request.content());
        ap.setLastEdited(request.lastEdited());
        ap.setApplication(jobApplication);

        return applicationNoteRepository.save(ap);
    }

    /**
     * Partially updates a note.
     *
     * @param applicationId the ID of the job application the note belongs to
     * @param notesId the ID of the note to update
     * @param request the request containing the fields to update
     * @param user the authenticated user updating the note
     * @return the updated note
     * @throws ForbiddenException if the user does not own the note
     */
    public ApplicationNote patch(Long applicationId, Long notesId, UpdateApplicationNoteRequest request, User user) {
        ApplicationNote toChange = applicationNoteRepository.findByNotesIdAndApplication_ApplicationIdAndApplication_User_UserId(notesId, request.application().getApplicationId(), user.getUserId())
                .orElseThrow(() -> new ForbiddenException("Not authorized to update this note"));

        if (request.content() != null) toChange.setContent(request.content());

        return applicationNoteRepository.save(toChange);
    }

    /**
     * Deletes a note by its ID.
     *
     * @param noteId the ID of the note to delete
     * @param applicationId the ID of the job application the note belongs to
     * @param user the authenticated user deleting the note
     * @throws ForbiddenException if the user does not own the note
     */
    public void delete(Long noteId, Long applicationId, User user) {
        ApplicationNote toDelete = applicationNoteRepository.findByNotesIdAndApplication_ApplicationIdAndApplication_User_UserId(noteId, applicationId, user.getUserId())
                .orElseThrow(() -> new ForbiddenException("Not authorized to delete this note"));

        applicationNoteRepository.deleteById(toDelete.getNotesId());
    }
}