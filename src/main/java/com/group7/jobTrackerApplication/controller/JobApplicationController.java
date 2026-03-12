package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.model.JobApplication;
import com.group7.jobTrackerApplication.service.JobApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/job-applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService){
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getAll(){
        return ResponseEntity.ok(jobApplicationService.getAll());
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<JobApplication> getById(@PathVariable Long applicationId){
        return ResponseEntity.ok(jobApplicationService.getById(applicationId));
    }

    @PostMapping
    public ResponseEntity<JobApplication> create(@AuthenticationPrincipal OAuth2User principal, @RequestBody JobApplication jobApplication){
        JobApplication created = jobApplicationService.create(principal, jobApplication);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<JobApplication> replace(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long applicationId, @RequestBody JobApplication jobApplication){
        JobApplication updated = jobApplicationService.replace(applicationId, principal, jobApplication);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{applicationId}")
    public ResponseEntity<JobApplication> patch(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long applicationId, @RequestBody Map<String, Object> updates){
        JobApplication patched = jobApplicationService.replace(applicationId, principal, updates);
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long applicationId){
        jobApplicationService.delete(applicationId, principal);
        return ResponseEntity.noContent().build();
    }
}