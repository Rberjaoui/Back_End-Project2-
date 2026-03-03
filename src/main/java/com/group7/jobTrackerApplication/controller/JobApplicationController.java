package com.group7.jobTrackerApplication.controller;

import com.group7.jobTrackerApplication.model.jobApplication;
import com.group7.jobTrackerApplication.service.jobApplicationService;
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
    public ReponseEntity<List<jobApplication>> getAll(){
        return ResponseEntity.ok(jobApplicationService.getAll());
    }

    @GetMapping("/{applicationId}")
    public ReponseEntity<jobApplication> getById(@PathVariable Long applicationId){
        return ResponseEntity.ok(jobApplicationService.getById());
    }

    @PostMapping
    public ResponseEntity<jobApplication> create(@RequestBody jobApplication jobApplication){
        JobApplication created = jobApplicationService.create(jobApplication);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<jobApplication> replace(@PathVariable Long applicationId, @RequestBody jobApplication jobApplication ){
        JobApplication updated = jobApplicationService.replace(applicationId, jobApplication);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{applicationId}")
    public ResponseEntity<jobApplication> patch(@PathVariable Long applicationId, @RequestBody Map<String, Object> updates){
        JobApplication patched = jobApplicationService.replace(applicationId, updates);
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<jobApplication> delete(@PathVariable Long applicationId){
        jobApplicationService.delete(applicationId);
        return ResponseEntity.noContent().build();
    }


}
