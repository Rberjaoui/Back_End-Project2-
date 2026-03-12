package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.DTO.CreateJobEntryRequest;
import com.group7.jobTrackerApplication.DTO.UpdateJobEntryRequest;
import com.group7.jobTrackerApplication.model.JobEntry;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.repository.JobEntryRepository;
import com.group7.jobTrackerApplication.exception.ResourceNotFoundException;
import com.group7.jobTrackerApplication.exception.ForbiddenException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobEntryService {

    private final JobEntryRepository jobEntryRepository;
    private final UserService userService;

    public JobEntryService(JobEntryRepository jobEntryRepository, UserService userService) {
        this.jobEntryRepository = jobEntryRepository;
        this.userService = userService;
    }

    public List<JobEntry> getAll() {
        return jobEntryRepository.findAll();
    }

    public JobEntry getById(Long jobId) {
        return jobEntryRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job entry not found with id: " + jobId));
    }

    public JobEntry create(OAuth2User principal, CreateJobEntryRequest request) {
        User user = userService.getOrCreateFromOAuth(principal);

        JobEntry je = new JobEntry();
        je.setCompanyName(request.CompanyName());
        je.setJobTitle(request.JobTitle());
        je.setSalaryText(request.SalaryText());
        je.setPostingURL(request.PostingUrl());
        je.setUserId(user.getUserId());

        return jobEntryRepository.save(je);
    }

    public JobEntry replace(Long jobId, OAuth2User principal, JobEntry jobEntry) {
        JobEntry existing = getById(jobId);
        User user = userService.getOrCreateFromOAuth(principal);

        if (!existing.getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("You do not have permission to update this job entry");
        }

        jobEntry.setJobId(jobId);
        return jobEntryRepository.save(jobEntry);
    }

    public JobEntry patch(Long jobId, OAuth2User principal, UpdateJobEntryRequest updates) {
        JobEntry existing = getById(jobId);
        User user = userService.getOrCreateFromOAuth(principal);

        if (!existing.getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("You do not have permission to update this job entry");
        }

        if (updates.getCompany() != null) existing.setCompanyName(updates.getCompany());
        if (updates.getJobName() != null) existing.setJobTitle(updates.getJobName());
        if (updates.getSalary() != null) existing.setSalaryText(updates.getSalary());
        if (updates.getPostingUrl() != null) existing.setPostingURL(updates.getPostingUrl());

        return jobEntryRepository.save(existing);
    }

    public void delete(Long jobId, OAuth2User principal) {
        JobEntry existing = getById(jobId);
        User user = userService.getOrCreateFromOAuth(principal);

        if (!existing.getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("You do not have permission to delete this job entry");
        }

        jobEntryRepository.deleteById(jobId);
    }
}