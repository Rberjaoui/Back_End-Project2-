package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.DTO.CreateJobEntryRequest;
import com.group7.jobTrackerApplication.DTO.GetJobEntryRequest;
import com.group7.jobTrackerApplication.DTO.UpdateJobEntryRequest;
import com.group7.jobTrackerApplication.model.JobEntry;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.repository.JobEntryRepository;
import com.group7.jobTrackerApplication.exception.ResourceNotFoundException;
import com.group7.jobTrackerApplication.exception.ForbiddenException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for managing job entries.
 * Handles business logic for creating, retrieving, updating, and deleting job entries.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@Service
public class JobEntryService {

    private final JobEntryRepository jobEntryRepository;
    private final UserService userService;

    /**
     * Constructs a JobEntryService with the given repositories.
     *
     * @param jobEntryRepository the repository used to manage job entries
     * @param userService the service used to manage users
     */
    public JobEntryService(JobEntryRepository jobEntryRepository, UserService userService) {
        this.jobEntryRepository = jobEntryRepository;
        this.userService = userService;
    }

    /**
     * Retrieves all job entries for the authenticated user.
     *
     * @param user the authenticated user requesting their job entries
     * @return a list of job entry summaries belonging to the user
     * @throws ResourceNotFoundException if no job entries are found for the user
     */
    public List<GetJobEntryRequest> getAll(User user) {
        List<JobEntry> entries = jobEntryRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Job entries not found"));

        return entries.stream()
                .map(entry -> new GetJobEntryRequest(
                        entry.getCompanyName(),
                        entry.getJobTitle(),
                        entry.getSalaryText(),
                        entry.getPostingURL()
                )).toList();
    }

    /**
     * Retrieves a job entry by its ID for the authenticated user.
     *
     * @param jobId the ID of the job entry to retrieve
     * @param user the authenticated user requesting the job entry
     * @return the job entry summary matching the given ID
     * @throws ResourceNotFoundException if no matching job entry is found
     */
    public GetJobEntryRequest getById(Long jobId, User user) {
        JobEntry entry = jobEntryRepository
                .findByJobIdAndUser_UserId(jobId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Job entry not found"));

        return new GetJobEntryRequest(
                entry.getCompanyName(),
                entry.getJobTitle(),
                entry.getSalaryText(),
                entry.getPostingURL()
        );
    }

    /**
     * Creates a new job entry for the authenticated user.
     *
     * @param principal the currently authenticated OAuth2 user
     * @param request the request containing the job entry details
     * @return the created job entry
     */
    public JobEntry create(OAuth2User principal, CreateJobEntryRequest request) {
        User user = userService.getOrCreateFromOAuth(principal);

        JobEntry je = new JobEntry();
        je.setCompanyName(request.companyName());
        je.setJobTitle(request.jobTitle());
        je.setSalaryText(request.salaryText());
        je.setPostingURL(request.postingURL());
        je.setUser(user);

        return jobEntryRepository.save(je);
    }

    /**
     * Replaces a job entry with new data.
     *
     * @param jobId the ID of the job entry to replace
     * @param request the request containing the updated job entry details
     * @param user the authenticated user updating the job entry
     * @return the updated job entry
     * @throws ForbiddenException if the user does not own the job entry
     */
    public JobEntry replace(Long jobId, UpdateJobEntryRequest request, User user) {
        JobEntry toChange = jobEntryRepository
                .findByJobIdAndUser_UserId(jobId, user.getUserId())
                .orElseThrow(() -> new ForbiddenException("Not authorized to update this job entry"));

        toChange.setCompanyName(request.companyName());
        toChange.setJobTitle(request.jobTitle());
        toChange.setPostingURL(request.postingURL());
        toChange.setSalaryText(request.salaryText());

        return jobEntryRepository.save(toChange);
    }

    /**
     * Partially updates a job entry.
     *
     * @param jobId the ID of the job entry to update
     * @param request the request containing the fields to update
     * @param user the authenticated user updating the job entry
     * @return the updated job entry
     * @throws ForbiddenException if the user does not own the job entry
     */
    public JobEntry patch(Long jobId, UpdateJobEntryRequest request, User user) {
        JobEntry toChange = jobEntryRepository.findByJobIdAndUser_UserId(jobId, user.getUserId())
                .orElseThrow(() -> new ForbiddenException("Not authorized to update this job entry"));

        if (request.companyName() != null) toChange.setCompanyName(request.companyName());
        if (request.jobTitle() != null) toChange.setJobTitle(request.jobTitle());
        if (request.salaryText() != null) toChange.setSalaryText(request.salaryText());
        if (request.postingURL() != null) toChange.setPostingURL(request.postingURL());

        return jobEntryRepository.save(toChange);
    }

    /**
     * Deletes a job entry by its ID.
     *
     * @param jobId the ID of the job entry to delete
     * @param user the authenticated user deleting the job entry
     * @throws ForbiddenException if the user does not own the job entry
     */
    public void delete(Long jobId, User user) {
        JobEntry toDelete = jobEntryRepository.findByJobIdAndUser_UserId(jobId, user.getUserId())
                .orElseThrow(() -> new ForbiddenException("Not authorized to delete this job entry"));

        jobEntryRepository.deleteById(toDelete.getJobId());
    }
}