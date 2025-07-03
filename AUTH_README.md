# CRUD App with Authentication

A Spring Boot REST API application with JWT-based authentication, user management, and CRUD operations for products.

## Features

- **User Authentication**: Email/password based authentication with JWT tokens
- **Password Management**: Forgot password functionality with email notifications
- **User Registration**: New user registration with email validation
- **Product CRUD**: Create, read, update, delete operations for products
- **Role-based Access Control**: Different permissions for users and admins
- **Email Integration**: Password reset and welcome emails
- **Token Refresh**: JWT token refresh mechanism

## Tech Stack

- **Backend**: Spring Boot 3.5.3, Java 21
- **Security**: Spring Security with JWT
- **Database**: PostgreSQL (NeonDB)
- **Email**: Spring Mail with SMTP
- **Validation**: Jakarta Validation
- **Documentation**: Built-in API endpoints

## Setup

### Prerequisites

- Java 21
- Maven
- PostgreSQL database (or NeonDB account)
- Email SMTP configuration (Gmail recommended)

### Configuration

1. **Database Configuration**
   Update `application.properties` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://your-db-host/your-db-name
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

2. **Email Configuration**
   Update email settings in `application.properties`:
   ```properties
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```

3. **JWT Configuration**
   The JWT secret is configured in `application.properties`. For production, use a strong secret key.

### Running the Application

```bash
# Using Maven
mvn spring-boot:run

# Or using Maven wrapper
./mvnw spring-boot:run  # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Forgot Password
```http
POST /api/auth/forgot-password
Content-Type: application/json

{
  "email": "user@example.com"
}
```

#### Reset Password
```http
POST /api/auth/reset-password
Content-Type: application/json

{
  "resetToken": "uuid-reset-token",
  "newPassword": "newpassword123"
}
```

#### Change Password
```http
POST /api/auth/change-password
Authorization: Bearer <token>
Content-Type: application/json

{
  "currentPassword": "oldpassword123",
  "newPassword": "newpassword123"
}
```

#### Get User Profile
```http
GET /api/auth/profile
Authorization: Bearer <token>
```

#### Refresh Token
```http
POST /api/auth/refresh-token
Content-Type: application/json

{
  "refreshToken": "your-refresh-token"
}
```

#### Validate Token
```http
POST /api/auth/validate-token
Authorization: Bearer <token>
```

### Product Endpoints

#### Get All Products
```http
GET /api/products
Authorization: Bearer <token>
```

#### Get Product by ID
```http
GET /api/products/{id}
Authorization: Bearer <token>
```

#### Create Product
```http
POST /api/products
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Product Name",
  "description": "Product Description",
  "price": 99.99
}
```

#### Update Product
```http
PUT /api/products/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Updated Product Name",
  "description": "Updated Description",
  "price": 149.99
}
```

#### Delete Product (Admin Only)
```http
DELETE /api/products/{id}
Authorization: Bearer <token>
```

## Security Configuration

- **JWT Token Expiration**: 24 hours
- **Refresh Token Expiration**: 7 days
- **Password Reset Token Expiration**: 1 hour
- **Password Encryption**: BCrypt
- **CORS**: Enabled for all origins (configure for production)

## User Roles

- **USER**: Can view, create, and update products
- **ADMIN**: Can perform all operations including delete products

## Email Templates

The application sends emails for:
- Welcome message upon registration
- Password reset instructions
- Password change confirmations

## Error Handling

The application includes comprehensive error handling with appropriate HTTP status codes and error messages.

## Development Notes

- Database schema is automatically created/updated using Hibernate DDL
- Expired reset tokens are automatically cleaned up every hour
- All passwords are encrypted using BCrypt
- JWT tokens include user information and role-based permissions

## Production Considerations

1. **Security**:
   - Change the JWT secret key
   - Configure CORS for specific origins
   - Use HTTPS in production
   - Set up proper email authentication

2. **Database**:
   - Use connection pooling
   - Configure proper database backup
   - Set appropriate DDL settings

3. **Monitoring**:
   - Configure logging levels
   - Set up application monitoring
   - Health check endpoints

## Testing

The application includes basic test structure. Run tests with:

```bash
mvn test
```

## API Testing with Postman/curl

Example curl commands:

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","firstName":"John","lastName":"Doe"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Get products (with token)
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```
