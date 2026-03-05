package com.group7.jobTrackerApplication.service;

import com.group7.jobTrackerApplication.model.ApplicationNote;
import com.group7.jobTrackerApplication.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApplicationNotesService {

    private final ApplicationNoteRepository applicationNoteRepository;

    public ApplicationNotesService(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public ApplicationNote getNoteById(Long noteId) {
        return applicationNoteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    public ApplicationNote create(ApplicationNote applicationNote) {
        return applicationNoteRepository.save(applicationNote);
    }

    public ApplicationNote patch(Long noteId, String newContent) {
        ApplicationNote note = getNoteById(noteId);
        note.setContent(newContent);
        return applicationNoteRepository.save(note);
    }

    public ApplicationNote delete(Long noteId) {
        ApplicationNote note = getNoteById(noteId);
        applicationNoteRepository.deleteById(noteId);
        return note;
    }
}