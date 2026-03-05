package com.group7.jobTrackerApplication.repository;

import com.group7.jobTrackerApplication.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUserId(Long userId);
}
