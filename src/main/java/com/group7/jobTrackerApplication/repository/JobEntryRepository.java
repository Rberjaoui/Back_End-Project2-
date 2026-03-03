package com.group7.jobTrackerApplication.repository;

import com.group7.jobTrackerApplication.model.JobEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobEntryRepository extends JpaRepository<JobEntry, Long> {

    List<JobEntry> findByUserId(Long userId);
}
