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

    public List<JobEntry> getAll( User user ) {
        return jobEntryRepository.findByUserId(user.getUserId());
    }

    public JobEntry getById(Long jobId, User user) {
        return jobEntryRepository
                .findByJobIdAndUserId(jobId, user.getUserId())
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

    public JobEntry replace(Long jobId, JobEntry changeTo, User user) {
        JobEntry toChange = jobEntryRepository
                        .findByJobIdAndUserId(jobId, user.getUserId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job entry not found"));

        toChange.setCompanyName(changeTo.getCompanyName());
        toChange.setJobTitle(changeTo.getJobTitle());
        toChange.setPostingURL(changeTo.getPostingURL());
        toChange.setSalaryText(changeTo.getSalaryText());

        return jobEntryRepository.save(toChange);
    }

    public JobEntry patch(Long jobId, UpdateJobEntryRequest updates, User user) {
        JobEntry toChange = jobEntryRepository.findByJobIdAndUserId(jobId, user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job entry not found"));

        if (updates.getCompany() != null) toChange.setCompanyName(updates.getCompany());
        if (updates.getJobName() != null) toChange.setJobTitle(updates.getJobName());
        if (updates.getSalary() != null) toChange.setSalaryText(updates.getSalary());
        if (updates.getPostingUrl() != null) toChange.setPostingURL(updates.getPostingUrl());

        return jobEntryRepository.save(toChange);
    }

    public void delete(Long jobId, User user) {
        JobEntry toDelete = jobEntryRepository.findByJobIdAndUserId(jobId, user.getUserId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job entry not found"));

        jobEntryRepository.deleteById(toDelete.getJobId());
    }
}