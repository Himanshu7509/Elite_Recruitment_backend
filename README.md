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
   - `MONGODB_URI`: Your MongoDB connection string (e.g., mongodb+srv://username:password@cluster.mongodb.net/database)

**Important Security Notes:**
- Never commit secrets to version control
- For local development, you can create a `.env` file with these variables

## Endpoints

### Authentication
- `POST /auth/student/signup` - User registration

### Student Form
- `POST /api/student-form/submit` - Submit student form

**Note:** The student form structure has been simplified. The `languages` field (which was a list of language abilities with read/write/speak capabilities) has been replaced with a single `language` string field.

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
# Run the application
./mvnw spring-boot:run
```

### Running with Environment Variables

Create a `.env` file in the project root with your configuration:

```env
MONGODB_URI=mongodb://localhost:27017/recruitmentDB
```

Then run with:
```bash
./mvnw spring-boot:run
```

The application will be available at http://localhost:8080