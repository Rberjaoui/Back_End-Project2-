package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.model.JobApplication;
import com.group7.jobTrackerApplication.model.User;
import com.group7.jobTrackerApplication.repository.JobApplicationRepository;
import com.group7.jobTrackerApplication.exception.ResourceNotFoundException;
import com.group7.jobTrackerApplication.exception.ForbiddenException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserService userService;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, UserService userService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userService = userService;
    }

    public List<JobApplication> getAll() {
        return jobApplicationRepository.findAll();
    }

    public JobApplication getById(Long applicationId) {
        return jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with id: " + applicationId));
    }

    public JobApplication create(OAuth2User principal, JobApplication jobApplication) {
        User user = userService.getOrCreateFromOAuth(principal);
        jobApplication.setUserId(user.getUserId());
        return jobApplicationRepository.save(jobApplication);
    }

    public JobApplication replace(Long applicationId, OAuth2User principal, JobApplication jobApplication) {
        JobApplication existing = getById(applicationId);
        User user = userService.getOrCreateFromOAuth(principal);

        if (!existing.getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("You do not have permission to update this application");
        }

        jobApplication.setApplicationId(applicationId);
        return jobApplicationRepository.save(jobApplication);
    }

    public JobApplication replace(Long applicationId, OAuth2User principal, Map<String, Object> updates) {
        JobApplication existing = getById(applicationId);
        User user = userService.getOrCreateFromOAuth(principal);

        if (!existing.getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("You do not have permission to update this application");
        }

        if (updates.containsKey("status")) {
            existing.setStatus((String) updates.get("status"));
        }
        if (updates.containsKey("dateApplied")) {
            existing.setDateApplied(LocalDate.parse((String) updates.get("dateApplied")));
        }

        return jobApplicationRepository.save(existing);
    }

    public void delete(Long applicationId, OAuth2User principal) {
        JobApplication existing = getById(applicationId);
        User user = userService.getOrCreateFromOAuth(principal);

        if (!existing.getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("You do not have permission to delete this application");
        }

        jobApplicationRepository.deleteById(applicationId);
    }
}