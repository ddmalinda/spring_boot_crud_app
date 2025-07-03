package com.dasith.crud_app.service;

import com.dasith.crud_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleanupService {

    private final UserRepository userRepository;

    @Scheduled(fixedRate = 3600000) // Run every hour
    @Transactional
    public void cleanupExpiredResetTokens() {
        try {
            userRepository.deleteByResetTokenExpiryBefore(LocalDateTime.now());
            log.debug("Cleaned up expired reset tokens");
        } catch (Exception e) {
            log.error("Error cleaning up expired reset tokens", e);
        }
    }
}
