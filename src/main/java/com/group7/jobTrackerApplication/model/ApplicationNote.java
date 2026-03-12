package com.group7.jobTrackerApplication.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application_notes")
public class ApplicationNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notes_id")
    private Long notesId;

    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content")
    private String content;

    @Column(name = "last_edited")
    private LocalDateTime lastEdited;

    public Long getNotesId() { return notesId; }
    public void setNotesId(Long notesId) { this.notesId = notesId; }

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getLastEdited() { return lastEdited; }
    public void setLastEdited(LocalDateTime lastEdited) { this.lastEdited = lastEdited; }
}