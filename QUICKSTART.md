# Quick Start Guide

This guide will help you get the License Management System up and running in 5 minutes.

## Prerequisites Check

Before starting, ensure you have:
- [ ] Java 17 or higher installed (`java -version`)
- [ ] Maven 3.6+ installed (`mvn -version`)

## Step-by-Step Setup

### 1. Navigate to Project Directory
```bash
cd license-management-system
```

### 2. Build the Project
```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the source code
- Run tests (if any)
- Create the executable JAR

### 3. Run the Application
```bash
mvn spring-boot:run
```

**That's it!** The application is now running.

## Verify Installation

### Check Application Status
Open your browser and visit:
- Application: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console

### H2 Console Login
- **JDBC URL**: `jdbc:h2:mem:licensedb`
- **Username**: `sa`
- **Password**: (leave empty)

### Test the API
Try this simple API call:
```bash
curl http://localhost:8080/api/users
```

You should see an empty array `[]` (no users yet).

## Create Your First Data

### 1. Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@company.com",
    "fullName": "System Administrator",
    "active": true
  }'
```

### 2. Create a Group
```bash
curl -X POST http://localhost:8080/api/groups \
  -H "Content-Type: application/json" \
  -d '{
    "name": "IT Department",
    "description": "Information Technology",
    "active": true
  }'
```

### 3. Create a License
```bash
curl -X POST http://localhost:8080/api/licenses \
  -H "Content-Type: application/json" \
  -d '{
    "softwareName": "Microsoft Office 365",
    "licenseKey": "OFFICE-2024-DEMO-001",
    "totalSeats": 50,
    "description": "Office Suite",
    "active": true
  }'
```

### 4. Assign License to User
```bash
curl -X POST http://localhost:8080/api/licenses/1/assign/user/1 \
  -H "Content-Type: application/json" \
  -d '{
    "notes": "Initial assignment"
  }'
```

## Load Sample Data (Optional)

To populate the database with sample data:

1. Access H2 Console: http://localhost:8080/h2-console
2. Login with credentials above
3. Open `sample-data.sql` file
4. Copy and paste the SQL statements
5. Execute them

## Use Postman Collection

1. Open Postman
2. Import `postman-collection.json`
3. Set the `baseUrl` variable to `http://localhost:8080/api`
4. Start testing all endpoints!

## Common Issues

### Port 8080 Already in Use
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Build Fails
Clean Maven cache:
```bash
mvn clean
rm -rf ~/.m2/repository
mvn install
```

### Application Won't Start
Check Java version:
```bash
java -version
```
Must be Java 17 or higher.

## Next Steps

- Read the full [README.md](README.md) for detailed documentation
- Explore all 23 API endpoints
- Set up PostgreSQL for production use
- Customize the application for your needs

## Support

If you encounter issues:
1. Check the console output for error messages
2. Review the [README.md](README.md) for detailed configuration
3. Verify all prerequisites are installed correctly

## Stop the Application

Press `Ctrl + C` in the terminal where the application is running.

---

**Congratulations!** You now have a fully functional License Management System running locally. 🎉
