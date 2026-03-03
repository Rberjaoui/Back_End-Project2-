package com.group7.jobTrackerApplication.repository;

import com.group7.jobTrackerApplication.model.ApplicationNote;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApplicationNoteRepository extends JpaRepository<ApplicationNote, Long>{

    List<ApplicationNote> findByApplicationId(Long applicationId);
}
