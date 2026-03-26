package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.DTO.CreateJobApplicationRequest;
import com.group7.jobTrackerApplication.DTO.GetJobApplicationRequest;
import com.group7.jobTrackerApplication.DTO.UpdateJobApplicationRequest;
import com.group7.jobTrackerApplication.model.JobApplication;
import com.group7.jobTrackerApplication.model.JobEntry;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.repository.JobApplicationRepository;
import com.group7.jobTrackerApplication.repository.JobEntryRepository;
import org.springframework.http.HttpStatus;
import com.group7.jobTrackerApplication.exception.ResourceNotFoundException;
import com.group7.jobTrackerApplication.exception.ForbiddenException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

/**
 * Service class for managing job applications.
 * Handles business logic for creating, retrieving, updating, and deleting job applications.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobEntryRepository jobEntryRepository;

    /**
     * Constructs a JobApplicationService with the given repositories.
     *
     * @param jobApplicationRepository the repository used to manage job applications
     * @param jobEntryRepository the repository used to manage job entries
     */
    public JobApplicationService(JobApplicationRepository jobApplicationRepository, JobEntryRepository jobEntryRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobEntryRepository = jobEntryRepository;
    }

    /**
     * Retrieves all job applications for the authenticated user.
     *
     * @param user the authenticated user requesting their applications
     * @return a list of job application summaries belonging to the user
     * @throws ResourceNotFoundException if no applications are found for the user
     */
    public List<GetJobApplicationRequest> getAll(User user) {
        List<JobApplication> applications = jobApplicationRepository.findAllByUser_UserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Job applications not found"));

        return applications.stream()
                .map(app -> new GetJobApplicationRequest(
                        app.getApplicationId(),
                        app.getJobEntry().getJobId(),
                        app.getJobEntry().getJobTitle(),
                        app.getStatus(),
                        app.getDateApplied(),
                        app.getNote() == null ? null : app.getNote().getNotesId()
                )).toList();
    }

    /**
     * Retrieves a job application by its ID for the authenticated user.
     *
     * @param applicationId the ID of the job application to retrieve
     * @param user the authenticated user requesting the application
     * @return the job application summary matching the given ID
     * @throws ResourceNotFoundException if no matching application is found
     */
    public GetJobApplicationRequest getById(Long applicationId, User user) {
        JobApplication application = jobApplicationRepository.findByApplicationIdAndUser_UserId(applicationId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));

        return new GetJobApplicationRequest(
                application.getApplicationId(),
                application.getJobEntry().getJobId(),
                application.getJobEntry().getJobTitle(),
                application.getStatus(),
                application.getDateApplied(),
                application.getNote() == null ? null : application.getNote().getNotesId()
        );
    }

    /**
     * Creates a new job application for the authenticated user.
     *
     * @param jobApplication the request containing the job application details
     * @param user the authenticated user creating the application
     * @return the created job application
     * @throws ResourceNotFoundException if the job entry is not found
     */
    public JobApplication create(CreateJobApplicationRequest jobApplication, User user) {
        JobEntry jobEntry = getOwnedJobEntry(jobApplication.jobId(), user);

        JobApplication jp = new JobApplication();
        jp.setDateApplied(jobApplication.dateApplied());
        jp.setStatus(jobApplication.status());
        jp.setUser(user);
        jp.setJobEntry(jobEntry);

        return jobApplicationRepository.save(jp);
    }

    /**
     * Replaces a job application with new data.
     *
     * @param applicationId the ID of the job application to replace
     * @param jobApplication the request containing the updated job application details
     * @param user the authenticated user updating the application
     * @return the updated job application
     * @throws ForbiddenException if the user does not own the job application
     */
    public JobApplication replace(Long applicationId, UpdateJobApplicationRequest jobApplication, User user) {
        JobApplication toChange = jobApplicationRepository.findByApplicationIdAndUser_UserId(applicationId, user.getUserId())
                .orElseThrow(() -> new ForbiddenException("Not authorized to update requested job application."));

        toChange.setDateApplied(jobApplication.dateApplied());
        toChange.setStatus(jobApplication.status());
        toChange.setJobEntry(getOwnedJobEntry(jobApplication.jobId(), user));

        return jobApplicationRepository.save(toChange);
    }

    /**
     * Partially updates a job application.
     *
     * @param applicationId the ID of the job application to update
     * @param request the request containing the fields to update
     * @param user the authenticated user updating the application
     * @return the updated job application
     * @throws ForbiddenException if the user does not own the job application
     */
    public JobApplication patch(Long applicationId, UpdateJobApplicationRequest request, User user) {
        JobApplication toChange = jobApplicationRepository.findByApplicationIdAndUser_UserId(applicationId, user.getUserId())
                .orElseThrow(() -> new ForbiddenException("Not authorized to update requested job application."));

        if (request.dateApplied() != null) toChange.setDateApplied(request.dateApplied());
        if (request.status() != null) toChange.setStatus(request.status());
        if (request.jobId() != null) toChange.setJobEntry(getOwnedJobEntry(request.jobId(), user));

        return jobApplicationRepository.save(toChange);
    }

    /**
     * Deletes a job application by its ID.
     *
     * @param applicationId the ID of the job application to delete
     * @param user the authenticated user deleting the application
     * @throws ForbiddenException if the user does not own the job application
     */
    public void delete(Long applicationId, User user) {
        JobApplication toDelete = jobApplicationRepository.findByApplicationIdAndUser_UserId(applicationId, user.getUserId())
                .orElseThrow(() -> new ForbiddenException("Not authorized to delete requested job application."));

        jobApplicationRepository.deleteById(toDelete.getApplicationId());
    }

    /**
     * Retrieves a job entry owned by the authenticated user.
     *
     * @param jobId the ID of the job entry to retrieve
     * @param user the authenticated user requesting the job entry
     * @return the job entry matching the given ID and user
     * @throws ResourceNotFoundException if no matching job entry is found
     */
    private JobEntry getOwnedJobEntry(Long jobId, User user) {
        return jobEntryRepository.findByJobIdAndUser_UserId(jobId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Job entry not found"));
    }
}