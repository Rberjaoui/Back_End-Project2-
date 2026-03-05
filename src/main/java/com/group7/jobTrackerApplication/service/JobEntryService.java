package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.model.JobEntry;
import com.group7.jobTrackerApplication.repository.JobEntryRepository;
import com.group7.jobTrackerApplication.DTO.UpdateJobEntryRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;


@Service
public class JobEntryService {

    private final JobEntryRepository jobEntryRepository;

    public JobEntryService(JobEntryRepository jobEntryRepository) {
        this.jobEntryRepository = jobEntryRepository;
    }

    public List<JobEntry> getAll() {
        return jobEntryRepository.findAll();
    }

    public JobEntry getById(Long jobId) {
        return jobEntryRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job entry not found"));
    }

    public JobEntry create(JobEntry jobEntry) {
        return jobEntryRepository.save(jobEntry);
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