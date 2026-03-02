package com.group7.jobTrackerApplication;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_entries")
public class JobEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "company")
    private String companyName;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "salary_text")
    private String salaryText;


    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getSalaryText() { return salaryText; }
    public void setSalaryText(String salaryText) { this.salaryText = salaryText; }
}