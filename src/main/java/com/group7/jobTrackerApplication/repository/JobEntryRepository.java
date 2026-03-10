package com.group7.jobTrackerApplication.repository;

import com.group7.jobTrackerApplication.model.JobEntry;
import com.group7.jobTrackerApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JobEntryRepository extends JpaRepository<JobEntry, Long> {

    List<JobEntry> findByUserId(Long userId);

    Optional<JobEntry> findByJobIdAndUserId(Long jobId, Long userId);
}
