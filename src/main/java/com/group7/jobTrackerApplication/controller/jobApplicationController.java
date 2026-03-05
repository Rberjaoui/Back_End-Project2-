package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.model.JobApplication;
import com.group7.jobTrackerApplication.service.JobApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/job-applications")
public class jobApplicationController {

    private final JobApplicationService jobApplicationService;

    public jobApplicationController( JobApplicationService jobApplicationService){
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
    public ResponseEntity<JobApplication> create(@RequestBody JobApplication jobApplication){
        JobApplication created = jobApplicationService.create(jobApplication);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<JobApplication> replace(@PathVariable Long applicationId, @RequestBody JobApplication jobApplication ){
        JobApplication updated = jobApplicationService.replace(applicationId, jobApplication);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{applicationId}")
    public ResponseEntity<JobApplication> patch(@PathVariable Long applicationId, @RequestBody Map<String, Object> updates){
        JobApplication patched = jobApplicationService.replace(applicationId, updates);
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<JobApplication> delete(@PathVariable Long applicationId){
        jobApplicationService.delete(applicationId);
        return ResponseEntity.noContent().build();
    }


}
