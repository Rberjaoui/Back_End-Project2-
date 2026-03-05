package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.DTO.UpdateJobEntryRequest;
import com.group7.jobTrackerApplication.DTO.CreateJobEntryRequest;
import com.group7.jobTrackerApplication.model.JobEntry;
import com.group7.jobTrackerApplication.service.JobEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-entries")
public class JobEntryController {

    private final JobEntryService jobEntryService;

    public JobEntryController( JobEntryService jobEntryService){
        this.jobEntryService = jobEntryService;
    }

    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal OAuth2User user) {
        return user == null ? "NOT AUTHENTICATED" : user.getAttributes();
    }

    @GetMapping
    public ResponseEntity<List<JobEntry>> getAll(){
        return ResponseEntity.ok(jobEntryService.getAll());
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobEntry> getById(@PathVariable Long jobId){
        return ResponseEntity.ok(jobEntryService.getById(jobId));
    }

    @PostMapping
    public ResponseEntity<JobEntry> create(@AuthenticationPrincipal OAuth2User principal, @RequestBody CreateJobEntryRequest request){
        JobEntry created = jobEntryService.create(principal, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobEntry> replace(@PathVariable Long jobId, @RequestBody JobEntry jobEntry ){
        JobEntry updated = jobEntryService.replace(jobId, jobEntry);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{jobId}")
    public ResponseEntity<JobEntry> patch(@PathVariable Long jobId, @RequestBody UpdateJobEntryRequest updates){
        JobEntry patched = jobEntryService.patch(jobId, updates);
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<JobEntry> delete(@PathVariable Long jobId){
        jobEntryService.delete(jobId);
        return ResponseEntity.noContent().build();
    }


}
