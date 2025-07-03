package com.dasith.crud_app.repository;

import com.dasith.crud_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<User> findByResetToken(String resetToken);
    
    void deleteByResetTokenExpiryBefore(LocalDateTime dateTime);
}
