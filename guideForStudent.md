# Spring Boot Authentication Project Study Guide

This guide will help you understand and learn the core concepts of this Spring Boot authentication project. Follow this structured approach to build a solid foundation in Spring Boot development through hands-on exploration of the codebase.

## 1. Project Overview

This Spring Boot application implements:
- User authentication with JWT tokens
- Password reset functionality with email notifications
- Role-based access control for API endpoints
- CRUD operations for products

## 2. Core Concepts to Master

### Spring Boot Fundamentals

#### 2.1 Dependency Injection & IoC
Spring automatically manages your objects (beans) and provides them where needed:

```java
@Service
public class AuthService {
    private final UserRepository userRepository;
    
    // Spring injects this dependency automatically
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**Learning Task**: Find all constructor-injected dependencies in the service classes.

#### 2.2 Spring Annotations
Annotations configure your application:

```java
@RestController         // Marks a class as a REST controller
@Service                // Marks a class as a service component
@Repository             // Marks a class as a data access component
@Entity                 // Marks a class as a database entity
@Transactional          // Applies transaction behavior
@RequestMapping("/api") // Maps requests to handler methods
```

**Learning Task**: Identify different types of annotations in the project and understand their purpose.

#### 2.3 Application Structure
The project follows a layered architecture:

```
├── controller/  // Handles HTTP requests
├── service/     // Contains business logic
├── repository/  // Manages data access
├── model/       // Defines database entities
├── dto/         // Data Transfer Objects
├── config/      // Configuration classes
└── util/        // Utility functions
```

**Learning Task**: Draw a diagram showing how these layers interact in the login flow.

### Authentication Implementation

#### 3.1 User Entity
The `User` class represents a user in the database:

```java
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    // Other fields and methods
}
```

**Learning Task**: Examine how the `User` implements `UserDetails` for Spring Security integration.

#### 3.2 JWT Implementation
JWT (JSON Web Token) provides stateless authentication:

```java
// Creating a token
String token = Jwts.builder()
    .setSubject(userDetails.getUsername())
    .setIssuedAt(new Date())
    .setExpiration(expiryDate)
    .signWith(key)
    .compact();
    
// Validating a token
Claims claims = Jwts.parserBuilder()
    .setSigningKey(key)
    .build()
    .parseClaimsJws(token)
    .getBody();
```

**Learning Task**: Trace how a token is created during login and validated during subsequent requests.

#### 3.3 Authentication Filter
The `JwtAuthenticationFilter` intercepts requests to validate tokens:

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) {
        // Extract and validate token
        // Set authentication in SecurityContext if valid
    }
}
```

**Learning Task**: Follow the filter's execution flow and understand how it sets user authentication.

### Data Access & API Handling

#### 4.1 Repositories
Spring Data JPA repositories provide database access:

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

**Learning Task**: List all custom query methods in the repositories and explain how Spring implements them.

#### 4.2 DTOs (Data Transfer Objects)
DTOs structure request and response data:

```java
// Request DTO
public class LoginRequest {
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    // Getters and setters
}

// Response DTO
public class AuthResponse {
    private String token;
    private String refreshToken;
    private Long userId;
    // Other fields and methods
}
```

**Learning Task**: Compare entity classes with their corresponding DTOs and note the differences.

#### 4.3 Controllers
Controllers handle HTTP requests:

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Authentication logic
        return ResponseEntity.ok(new AuthResponse(...));
    }
}
```

**Learning Task**: Map out all endpoints in the application and their corresponding controller methods.

### Security Configuration

#### 5.1 SecurityConfig
The security configuration defines authentication and authorization rules:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            // More configuration
        return http.build();
    }
}
```

**Learning Task**: Identify which endpoints are publicly accessible and which require authentication.

#### 5.2 Password Encryption
BCrypt is used for secure password storage:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**Learning Task**: Find where passwords are encoded before storage and where they're verified during login.

## 3. Practical Exercises

Work through these exercises to deepen your understanding:

### Exercise 1: Trace Authentication Flow
1. Start at the login endpoint in `AuthController`
2. Follow the execution to `AuthService`
3. Understand token generation in `JwtUtil`
4. See how `SecurityConfig` configures authentication
5. Trace how `JwtAuthenticationFilter` validates tokens

### Exercise 2: Add a New Field
Add a "phoneNumber" field to the user profile:
1. Update the `User` entity
2. Modify the registration request DTO
3. Update the user profile response DTO
4. Add validation for the phone number

### Exercise 3: Create a New Endpoint
Add an endpoint to change a user's email:
1. Create a request DTO for email change
2. Add a method to `AuthService`
3. Implement the controller endpoint
4. Add proper validation and error handling

### Exercise 4: Implement Account Locking
Add security for failed login attempts:
1. Add a "failedAttempts" field to `User`
2. Update login logic to track failed attempts
3. Lock accounts after 5 failed attempts
4. Create an endpoint to unlock accounts

### Exercise 5: Study and Enhance Error Handling
1. Examine `GlobalExceptionHandler`
2. Add custom exceptions for specific error cases
3. Implement consistent error responses

## 4. Understanding the Database

The application uses JPA to interact with the database:

```java
// Creating/updating records
User user = new User();
user.setEmail(email);
user.setPassword(encodedPassword);
userRepository.save(user);

// Querying
Optional<User> userOpt = userRepository.findByEmail(email);
```

**Learning Task**: Experiment with H2 console to view the database structure.

## 5. Testing the API

Use tools like Postman or curl to test the API:

```bash
# Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","firstName":"John","lastName":"Doe"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

**Learning Task**: Create a collection of API tests for all endpoints.

## 6. Advanced Concepts

Once you're comfortable with the basics, explore:

### 6.1 Refresh Token Mechanism
Understand how refresh tokens provide secure long-term authentication.

### 6.2 Email Service Integration
Study how the application sends emails for password reset.

### 6.3 Scheduled Tasks
Explore how expired tokens are automatically cleaned up.

### 6.4 Spring Profiles
Learn how to configure different environments (dev, test, prod).

## 7. Study Checklist

- [ ] Understand dependency injection
- [ ] Map the application architecture
- [ ] Trace the authentication flow
- [ ] Understand JWT creation and validation
- [ ] Study the data access layer (repositories)
- [ ] Examine controllers and request handling
- [ ] Understand security configuration
- [ ] Complete practical exercises
- [ ] Test all API endpoints
- [ ] Explore advanced concepts

## 8. Resources for Further Learning

* [Official Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
* [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html)
* [JWT Authentication Tutorial](https://www.toptal.com/spring/spring-security-tutorial)
* [Spring Data JPA Guide](https://spring.io/guides/gs/accessing-data-jpa/)
* [REST API Design Best Practices](https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design)

## 9. Debugging Tips

1. Enable DEBUG level logging:
```properties
logging.level.com.dasith.crud_app=DEBUG
logging.level.org.springframework.security=DEBUG
```

2. Use breakpoints in controllers and service methods
3. Check JWT token contents at [jwt.io](https://jwt.io)
4. Use H2 console to inspect database state

Remember that understanding this project will give you a solid foundation in Spring Boot development, especially for secure REST APIs. Take your time to explore each component and how they work together.
