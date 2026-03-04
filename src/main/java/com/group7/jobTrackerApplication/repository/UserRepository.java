package com.group7.jobTrackerApplication.repository;

import com.group7.jobTrackerApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
