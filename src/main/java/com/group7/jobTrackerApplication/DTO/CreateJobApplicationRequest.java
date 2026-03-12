package com.group7.jobTrackerApplication.DTO;

import java.time.LocalDate;

public record CreateJobApplicationRequest(Long jobId, String status, LocalDate dateApplied) {
}
