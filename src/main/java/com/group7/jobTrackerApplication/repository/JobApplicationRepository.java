package com.group7.jobTrackerApplication.repository;

import com.group7.jobTrackerApplication.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Optional<List<JobApplication>> findByUserId(Long userId);

    Optional<JobApplication> findByApplicationIdAndUserId(Long applicationId, Long userId );
}
