package com.dasith.crud_app.controller;

import com.dasith.crud_app.dto.*;
import com.dasith.crud_app.model.User;
import com.dasith.crud_app.service.AuthService;
import com.dasith.crud_app.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.registerUser(
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User registered successfully"));
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = authService.authenticateUser(request.getEmail(), request.getPassword());
            User user = authService.getUserByEmail(request.getEmail());
            
            // Generate refresh token
            UserDetails userDetails = authService.loadUserByUsername(request.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            AuthResponse authResponse = new AuthResponse(
                    token,
                    refreshToken,
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName()
            );

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            authService.initiatePasswordReset(request.getEmail());
            return ResponseEntity.ok(new ApiResponse(true, "Password reset email sent successfully"));
        } catch (Exception e) {
            log.error("Forgot password failed: {}", e.getMessage());
            // Don't reveal if email exists or not for security reasons
            return ResponseEntity.ok(new ApiResponse(true, "If the email exists, a password reset link has been sent"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getResetToken(), request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
        } catch (Exception e) {
            log.error("Password reset failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                                     Authentication authentication) {
        try {
            String email = authentication.getName();
            authService.changePassword(email, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
        } catch (Exception e) {
            log.error("Password change failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String newToken = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(new ApiResponse(true, "Token refreshed successfully", newToken));
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = authService.getUserByEmail(email);
            
            // Create a safe user response without password
            UserProfileResponse userProfile = new UserProfileResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole().name(),
                    user.getCreatedAt()
            );
            
            return ResponseEntity.ok(new ApiResponse(true, "Profile retrieved successfully", userProfile));
        } catch (Exception e) {
            log.error("Profile retrieval failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                boolean isValid = jwtUtil.validateToken(token);
                
                if (isValid) {
                    return ResponseEntity.ok(new ApiResponse(true, "Token is valid"));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ApiResponse(false, "Token is invalid"));
                }
            } else {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Authorization header is missing or invalid"));
            }
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Token validation failed"));
        }
    }
}
