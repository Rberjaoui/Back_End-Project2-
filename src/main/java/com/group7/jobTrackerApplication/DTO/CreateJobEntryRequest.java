package com.group7.jobTrackerApplication.DTO;

public record CreateJobEntryRequest(
        String CompanyName,
        String SalaryText,
        String PostingUrl,
        String JobTitle
) {}
