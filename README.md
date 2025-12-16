# Elite Aptitude System

A Spring Boot application for managing aptitude tests and student information.

## Prerequisites

- Docker
- Railway account

## Deployment to Railway

1. Push this repository to GitHub
2. Connect your GitHub repository to Railway
3. Railway will automatically detect the Dockerfile and build the application
4. Set the following environment variables in Railway:
   - `MONGODB_URI`: Your MongoDB connection string
   - `JWT_SECRET`: Your JWT secret key
   - `JWT_EXPIRATION_MS`: JWT expiration time in milliseconds (optional, defaults to 86400000)

## Endpoints

### Authentication
- `POST /auth/student/signup` - User registration

### Student Form
- `POST /api/student-form/submit` - Submit student form

## Local Development

### Running with Docker

```bash
# Build the Docker image
docker build -t elite-aptitude-system .

# Run the container
docker run -p 8080:8080 elite-aptitude-system
```

### Running with Maven

```bash
# Navigate to the demo directory
cd demo

# Run the application
./mvnw spring-boot:run
```

The application will be available at http://localhost:8080