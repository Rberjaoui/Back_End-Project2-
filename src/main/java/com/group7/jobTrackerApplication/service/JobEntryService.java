package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.model.JobEntry;
import com.group7.jobTrackerApplication.repository.JobEntryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

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

    public JobEntry replace(Long jobId, Map<String, Object> updates) {
        JobEntry existing = getById(jobId);

        if (updates.containsKey("jobTitle")) {
            existing.setJobTitle((String) updates.get("jobTitle"));
        }
        if (updates.containsKey("companyName")) {
            existing.setCompanyName((String) updates.get("companyName"));
        }
        if (updates.containsKey("salaryText")) {
            existing.setSalaryText((String) updates.get("salaryText"));
        }

        return jobEntryRepository.save(existing);
    }

    public void delete(Long jobId) {
        jobEntryRepository.deleteById(jobId);
    }
}