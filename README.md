# License Management System

Enterprise-grade License Management System built with Spring Boot for tracking and managing software license assignments across users and groups.

## Features

- **Complete CRUD Operations** for Users, Groups, and Licenses
- **Flexible Many-to-Many Relationships** between Users and Groups
- **License Assignment** to individual users and groups
- **Comprehensive Audit Trail** with 13 different action types
- **Automatic History Tracking** for all license operations
- **Seat Management** with automatic allocation and validation
- **REST API** with 23 endpoints across 3 controllers
- **Multi-Database Support** (H2 for development, PostgreSQL for production)

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (Development)
- **PostgreSQL** (Production)
- **Lombok** (Optional)
- **Maven** (Build Tool)

## Project Structure

```
license-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/license/management/
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java
│   │   │   │   ├── UserGroupController.java
│   │   │   │   └── LicenseController.java
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── UserGroupService.java
│   │   │   │   └── LicenseService.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── UserGroupRepository.java
│   │   │   │   ├── LicenseRepository.java
│   │   │   │   ├── UserLicenseRepository.java
│   │   │   │   ├── GroupLicenseRepository.java
│   │   │   │   └── LicenseHistoryRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── UserGroup.java
│   │   │   │   ├── License.java
│   │   │   │   ├── UserLicense.java
│   │   │   │   ├── GroupLicense.java
│   │   │   │   └── LicenseHistory.java
│   │   │   └── LicenseManagementApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-prod.properties
│   └── test/
└── pom.xml
```

## Database Schema

### Core Entities

- **users**: User information with username, email, full name
- **user_groups**: Group information for organizing users
- **licenses**: Software licenses with seat management
- **user_licenses**: Junction table for user-license assignments
- **group_licenses**: Junction table for group-license assignments with seat allocation
- **license_history**: Comprehensive audit trail for all license operations
- **user_group_mapping**: Many-to-Many mapping between users and groups

### Key Relationships

- Users ↔ Groups (Many-to-Many)
- Users ↔ Licenses (Many-to-Many via UserLicense)
- Groups ↔ Licenses (Many-to-Many via GroupLicense)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL (for production)

### Installation

1. **Clone or extract the project**
   ```bash
   cd license-management-system
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run in Development Mode (H2 Database)**
   ```bash
   mvn spring-boot:run
   ```
   
   The application will start on `http://localhost:8080`
   H2 Console available at: `http://localhost:8080/h2-console`

4. **Run in Production Mode (PostgreSQL)**
   
   First, configure PostgreSQL in `src/main/resources/application-prod.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/licensedb
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
   
   Then run:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

## API Endpoints

### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/username/{username}` | Get user by username |
| GET | `/api/users/active` | Get all active users |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| POST | `/api/users/{userId}/groups/{groupId}` | Add user to group |
| DELETE | `/api/users/{userId}/groups/{groupId}` | Remove user from group |

### Group Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/groups` | Get all groups |
| GET | `/api/groups/{id}` | Get group by ID |
| GET | `/api/groups/name/{name}` | Get group by name |
| GET | `/api/groups/active` | Get all active groups |
| POST | `/api/groups` | Create new group |
| PUT | `/api/groups/{id}` | Update group |
| DELETE | `/api/groups/{id}` | Delete group |

### License Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/licenses` | Get all licenses |
| GET | `/api/licenses/{id}` | Get license by ID |
| GET | `/api/licenses/active` | Get all active licenses |
| GET | `/api/licenses/available` | Get licenses with available seats |
| POST | `/api/licenses` | Create new license |
| PUT | `/api/licenses/{id}` | Update license |
| DELETE | `/api/licenses/{id}` | Delete license |

### License Assignment

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/licenses/{licenseId}/assign/user/{userId}` | Assign license to user |
| DELETE | `/api/licenses/user-assignments/{userLicenseId}` | Revoke license from user |
| GET | `/api/licenses/user/{userId}` | Get all licenses for a user |
| GET | `/api/licenses/{licenseId}/users` | Get all users for a license |
| POST | `/api/licenses/{licenseId}/assign/group/{groupId}` | Assign license to group |
| DELETE | `/api/licenses/group-assignments/{groupLicenseId}` | Revoke license from group |
| GET | `/api/licenses/group/{groupId}` | Get all licenses for a group |
| GET | `/api/licenses/{licenseId}/groups` | Get all groups for a license |

### Audit & History

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/licenses/{licenseId}/history` | Get history for a license |
| GET | `/api/licenses/history/user/{userId}` | Get history for a user |
| GET | `/api/licenses/history/recent` | Get recent history (last 50) |

## API Examples

### Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john.doe@example.com",
    "fullName": "John Doe",
    "active": true
  }'
```

### Create a License
```bash
curl -X POST http://localhost:8080/api/licenses \
  -H "Content-Type: application/json" \
  -d '{
    "softwareName": "Microsoft Office 365",
    "licenseKey": "XXXXX-XXXXX-XXXXX-XXXXX",
    "totalSeats": 100,
    "description": "Enterprise license",
    "active": true
  }'
```

### Assign License to User
```bash
curl -X POST http://localhost:8080/api/licenses/1/assign/user/1 \
  -H "Content-Type: application/json" \
  -d '{
    "notes": "Assigned for project work"
  }'
```

### Assign License to Group
```bash
curl -X POST http://localhost:8080/api/licenses/1/assign/group/1 \
  -H "Content-Type: application/json" \
  -d '{
    "allocatedSeats": 25,
    "notes": "Allocated for Engineering Department"
  }'
```

## Audit Trail

The system automatically tracks the following actions:

1. LICENSE_CREATED
2. LICENSE_UPDATED
3. LICENSE_DELETED
4. LICENSE_ASSIGNED_TO_USER
5. LICENSE_REVOKED_FROM_USER
6. LICENSE_ASSIGNED_TO_GROUP
7. LICENSE_REVOKED_FROM_GROUP
8. GROUP_ALLOCATION_INCREASED
9. GROUP_ALLOCATION_DECREASED
10. LICENSE_EXPIRED
11. LICENSE_RENEWED
12. SEATS_INCREASED
13. SEATS_DECREASED

Each history entry includes:
- Timestamp
- Action type
- Description
- Details
- Associated license, user, or group IDs
- Performed by (user identifier)

## Configuration

### Development (H2)
Edit `src/main/resources/application.properties`

### Production (PostgreSQL)
Edit `src/main/resources/application-prod.properties`

Key configurations:
- Database connection URL
- Database credentials
- JPA/Hibernate settings
- Logging levels

## Building for Production

Create an executable JAR:
```bash
mvn clean package
```

Run the JAR:
```bash
java -jar target/license-management-system-1.0.0.jar --spring.profiles.active=prod
```

## Testing

The H2 console is available in development mode at:
`http://localhost:8080/h2-console`

Connection settings:
- JDBC URL: `jdbc:h2:mem:licensedb`
- Username: `sa`
- Password: (leave empty)

## Future Enhancements

- [ ] User authentication and authorization (Spring Security)
- [ ] Role-based access control (RBAC)
- [ ] License expiration notifications
- [ ] Usage analytics and reporting
- [ ] Bulk operations
- [ ] CSV import/export
- [ ] REST API documentation (Swagger/OpenAPI)
- [ ] Email notifications
- [ ] License compliance monitoring

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please create an issue in the repository.
