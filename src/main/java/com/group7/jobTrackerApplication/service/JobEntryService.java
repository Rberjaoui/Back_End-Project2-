package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.DTO.CreateJobEntryRequest;
import com.group7.jobTrackerApplication.model.JobEntry;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.repository.JobEntryRepository;
import com.group7.jobTrackerApplication.DTO.UpdateJobEntryRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job entry not found"));
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

    public JobEntry replace(Long jobId, JobEntry jobEntry) {
        jobEntry.setJobId(jobId);
        return jobEntryRepository.save(jobEntry);
    }

    public JobEntry patch(Long jobId, UpdateJobEntryRequest updates) {
        JobEntry existing = jobEntryRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (updates.getCompany() != null) existing.setCompanyName(updates.getCompany());
        if (updates.getJobName() != null) existing.setJobTitle(updates.getJobName());
        if (updates.getSalary() != null) existing.setSalaryText(updates.getSalary());
        if (updates.getPostingUrl() != null) existing.setPostingURL(updates.getPostingUrl());

        return jobEntryRepository.save(existing);
    }

    public void delete(Long jobId) {
        jobEntryRepository.deleteById(jobId);
    }
}