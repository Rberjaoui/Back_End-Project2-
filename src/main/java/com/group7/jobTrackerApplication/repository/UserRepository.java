package com.group7.jobTrackerApplication.repository;

import com.group7.jobTrackerApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthProviderAndOauthSubject(String oauthProvider, String oauthSubject);
}
