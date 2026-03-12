package com.group7.jobTrackerApplication.DTO;

import com.group7.jobTrackerApplication.model.JobApplication;

import java.time.LocalDateTime;

public record CreateApplicationNoteRequest(String content, LocalDateTime lastEdited, JobApplication jobApplication) {
}
