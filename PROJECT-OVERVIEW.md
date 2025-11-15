# License Management System - Project Overview

## 📋 What You Have

This is a **complete, production-ready Spring Boot application** for managing software licenses across your organization.

### Key Features ✨

- **23 REST API Endpoints** across 3 controllers
- **Complete CRUD Operations** for Users, Groups, and Licenses
- **Many-to-Many Relationships** (Users can belong to multiple groups)
- **Comprehensive Audit Trail** with 13 action types
- **Automatic History Tracking** for compliance
- **Seat Management** with automatic validation
- **Multi-Database Support** (H2 for dev, PostgreSQL for prod)

### What's Included 📦

```
license-management-system/
├── 📄 README.md                     - Complete documentation
├── 📄 QUICKSTART.md                 - 5-minute setup guide
├── 📄 API-DOCUMENTATION.md          - All 23 API endpoints
├── 📄 DATABASE-SCHEMA.md            - Database structure
├── 📄 pom.xml                       - Maven configuration
├── 📄 .gitignore                    - Git ignore file
├── 📄 sample-data.sql               - Sample test data
├── 📄 postman-collection.json       - API testing collection
│
├── 📁 src/main/java/com/license/management/
│   ├── 📁 controller/               - 3 REST Controllers
│   │   ├── UserController.java
│   │   ├── UserGroupController.java
│   │   └── LicenseController.java
│   │
│   ├── 📁 service/                  - Business Logic Layer
│   │   ├── UserService.java
│   │   ├── UserGroupService.java
│   │   └── LicenseService.java
│   │
│   ├── 📁 repository/               - 6 JPA Repositories
│   │   ├── UserRepository.java
│   │   ├── UserGroupRepository.java
│   │   ├── LicenseRepository.java
│   │   ├── UserLicenseRepository.java
│   │   ├── GroupLicenseRepository.java
│   │   └── LicenseHistoryRepository.java
│   │
│   ├── 📁 entity/                   - 6 Domain Entities
│   │   ├── User.java
│   │   ├── UserGroup.java
│   │   ├── License.java
│   │   ├── UserLicense.java
│   │   ├── GroupLicense.java
│   │   └── LicenseHistory.java
│   │
│   └── LicenseManagementApplication.java - Main class
│
└── 📁 src/main/resources/
    ├── application.properties        - Dev config (H2)
    └── application-prod.properties   - Prod config (PostgreSQL)
```

## 🚀 Quick Start (3 Steps)

### Step 1: Prerequisites
Make sure you have:
- ☑️ Java 17+ installed
- ☑️ Maven 3.6+ installed

### Step 2: Build & Run
```bash
cd license-management-system
mvn clean install
mvn spring-boot:run
```

### Step 3: Verify
Open browser: http://localhost:8080/h2-console

**That's it!** Your License Management System is running! 🎉

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| **README.md** | Complete project documentation |
| **QUICKSTART.md** | Get running in 5 minutes |
| **API-DOCUMENTATION.md** | All 23 REST endpoints with examples |
| **DATABASE-SCHEMA.md** | Database structure and relationships |

## 🔌 API Endpoints Summary

### User Management (9 endpoints)
- Create, Read, Update, Delete users
- Manage user-group relationships

### Group Management (7 endpoints)
- Create, Read, Update, Delete groups
- Query active groups

### License Management (7 endpoints)
- Create, Read, Update, Delete licenses
- Check available licenses
- Assign to users or groups
- Revoke assignments
- Complete audit trail

## 💾 Database Support

### Development (Default)
- **H2 In-Memory Database**
- No setup required
- H2 Console: http://localhost:8080/h2-console
- Perfect for testing and development

### Production
- **PostgreSQL**
- Configure in `application-prod.properties`
- Run with: `mvn spring-boot:run -Dspring-boot.run.profiles=prod`

## 🧪 Testing the API

### Option 1: Using cURL
```bash
# Get all users
curl http://localhost:8080/api/users

# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@company.com","fullName":"Administrator","active":true}'
```

### Option 2: Using Postman
1. Import `postman-collection.json` into Postman
2. Set baseUrl to `http://localhost:8080/api`
3. Test all 23 endpoints!

### Option 3: Using Sample Data
1. Open H2 Console: http://localhost:8080/h2-console
2. Open `sample-data.sql`
3. Copy and paste the SQL
4. Execute to populate test data

## 🏗️ Architecture

This follows Spring Boot best practices:

```
┌─────────────┐
│ Controllers │  ← REST API Layer (HTTP endpoints)
└──────┬──────┘
       │
┌──────▼──────┐
│  Services   │  ← Business Logic Layer
└──────┬──────┘
       │
┌──────▼────────┐
│ Repositories  │  ← Data Access Layer (JPA)
└──────┬────────┘
       │
┌──────▼────────┐
│   Database    │  ← H2 / PostgreSQL
└───────────────┘
```

## 🎯 Use Cases

Perfect for:
- ✅ Software license tracking
- ✅ User access management
- ✅ Department/team organization
- ✅ Compliance and auditing
- ✅ Seat management
- ✅ License utilization reporting

## 📈 What's Next?

The system is production-ready, but you can enhance it with:

- [ ] User authentication (Spring Security)
- [ ] Role-based access control
- [ ] Email notifications for expirations
- [ ] Usage analytics dashboard
- [ ] Bulk operations
- [ ] CSV import/export
- [ ] Swagger/OpenAPI documentation
- [ ] Docker containerization

## 🛠️ Customization

### Change Port
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Add More Fields
1. Update Entity classes in `src/main/java/.../entity/`
2. Spring Boot auto-creates database schema
3. No manual SQL needed!

### Add Business Logic
1. Update Service classes in `src/main/java/.../service/`
2. Services are where all business rules live

## 🐛 Troubleshooting

### Port Already in Use
Change port in `application.properties` or kill process:
```bash
# Find process
lsof -i :8080
# Kill process
kill -9 <PID>
```

### Build Fails
Clean Maven cache:
```bash
mvn clean
rm -rf ~/.m2/repository
mvn install
```

### Database Issues
Check H2 Console login:
- URL: `jdbc:h2:mem:licensedb`
- User: `sa`
- Password: (empty)

## 📞 Support

1. Check **README.md** for detailed docs
2. Review **API-DOCUMENTATION.md** for endpoint details
3. See **DATABASE-SCHEMA.md** for data structure
4. Check console logs for error messages

## 📝 License

MIT License - Feel free to use and modify as needed!

---

**Built with:**
- Spring Boot 3.2.0
- Java 17
- Spring Data JPA
- H2 Database / PostgreSQL
- Maven

**Happy License Management!** 🚀
