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
   - `JWT_SECRET`: Your JWT secret key (must be at least 32 characters long for security)
   - `JWT_EXPIRATION_MS`: JWT expiration time in milliseconds (optional, defaults to 86400000)

**Important Security Notes:**
- The JWT_SECRET should be a strong, random string of at least 32 characters
- Never commit secrets to version control
- For local development, you can create a `.env` file with these variables

## Endpoints

### Student Form
- `POST /api/student-form/submit` - Submit student form

**Note:** The student form structure has been simplified. The `languages` field (which was a list of language abilities with read/write/speak capabilities) has been replaced with a single `language` string field.

**Additional Fields Added:**
- `primarySkills` (List<String>): List of primary technical skills of the candidate
- `secondarySkills` (List<String>): List of secondary skills of the candidate
- `yearsOfExperience` (Double): Years of professional experience (allows fractional years)

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

### Example Student Form Request

**Method:** POST  
**URL:** `/api/student-form/submit`

**Headers:**
- Content-Type: application/json

**Body (JSON):**
```json
{
  "fullName": "John Doe",
  "fatherName": "Robert Doe",
  "postAppliedFor": "Software Engineer",
  "referenceName": "Jane Smith",
  "dateOfBirth": "1995-05-15",
  "age": 28,
  "maritalStatus": "Single",
  "sex": "Male",
  "linkedInProfile": "https://linkedin.com/in/johndoe",
  "language": "English, Spanish",
  "primarySkills": ["Java", "Spring Boot", "MongoDB"],
  "secondarySkills": ["React", "Docker", "AWS"],
  "yearsOfExperience": 5.5,
  "permanentAddressLine": "123 Main Street",
  "permanentPin": "123456",
  "permanentPhone": "+1-555-123-4567",
  "permanentEmail": "john.doe@example.com",
  "reference1Name": "Alice Johnson",
  "reference1Mobile": "+1-555-987-6543",
  "reference2Name": "Bob Wilson",
  "reference2Mobile": "+1-555-456-7890",
  "academicRecords": [
    {
      "schoolOrCollege": "ABC University",
      "boardOrUniversity": "XYZ Board",
      "examinationPassed": "Bachelor's Degree",
      "yearOfPassing": "2020",
      "mainSubjects": "Computer Science",
      "percentage": 85.5
    }
  ],
  "workExperiences": [
    {
      "employerName": "Tech Solutions Inc.",
      "durationFrom": "2020-06-01",
      "durationTo": "2023-05-31",
      "designation": "Software Developer",
      "briefJobProfile": "Developed web applications using Java and Spring Boot",
      "totalSalary": 75000,
      "joiningDate": "2020-06-01",
      "lastDate": "2023-05-31"
    }
  ]
}
```

### Running with Environment Variables

Create a `.env` file in the project root with your configuration:

```env
MONGODB_URI=mongodb://localhost:27017/recruitmentDB
JWT_SECRET=5aA3XkK9wE7yR2sT8uV4zQ6cF1gH9jL3mN8pQ2sT5vY8xZ1cE4rW7uA0bD3fG6hJ
JWT_EXPIRATION_MS=86400000
```

Then run with:
```bash
./mvnw spring-boot:run
```

The application will be available at http://localhost:8080