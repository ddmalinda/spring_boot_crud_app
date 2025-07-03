package com.dasith.crud_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@crudapp.com}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request");
            
            String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
            String emailContent = "Hello,\n\n" +
                    "You have requested to reset your password. Please click the link below to reset your password:\n\n" +
                    resetUrl + "\n\n" +
                    "This link will expire in 1 hour.\n\n" +
                    "If you did not request this password reset, please ignore this email.\n\n" +
                    "Best regards,\n" +
                    "CRUD App Team";
            
            message.setText(emailContent);
            
            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending password reset email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    public void sendWelcomeEmail(String toEmail, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to CRUD App!");
            
            String emailContent = "Hello " + firstName + ",\n\n" +
                    "Welcome to CRUD App! Your account has been successfully created.\n\n" +
                    "You can now log in to your account using your email and password.\n\n" +
                    "Best regards,\n" +
                    "CRUD App Team";
            
            message.setText(emailContent);
            
            mailSender.send(message);
            log.info("Welcome email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending welcome email to: {}", toEmail, e);
            // Don't throw exception for welcome email failure
        }
    }
}
