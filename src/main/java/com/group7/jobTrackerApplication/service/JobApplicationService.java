package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.model.JobApplication;
import com.group7.jobTrackerApplication.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public List<JobApplication> getAll() {
        return jobApplicationRepository.findAll();
    }

    public JobApplication getById(Long applicationId) {
        return jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Job application not found"));
    }

    public JobApplication create(JobApplication jobApplication) {
        return jobApplicationRepository.save(jobApplication);
    }

    public JobApplication replace(Long applicationId, JobApplication jobApplication) {
        jobApplication.setApplicationId(applicationId);
        return jobApplicationRepository.save(jobApplication);
    }

    public void delete(Long applicationId) {
        jobApplicationRepository.deleteById(applicationId);
    }
}