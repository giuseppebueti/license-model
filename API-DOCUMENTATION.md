# API Documentation

Complete REST API documentation for the License Management System.

**Base URL**: `http://localhost:8080/api`

---

## Table of Contents

1. [User Management APIs](#user-management-apis)
2. [Group Management APIs](#group-management-apis)
3. [License Management APIs](#license-management-apis)
4. [License Assignment APIs](#license-assignment-apis)
5. [Audit & History APIs](#audit--history-apis)
6. [Error Handling](#error-handling)

---

## User Management APIs

### 1. Get All Users
Retrieve a list of all users in the system.

**Endpoint**: `GET /users`

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "username": "john.doe",
    "email": "john.doe@company.com",
    "fullName": "John Doe",
    "active": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00",
    "groups": []
  }
]
```

---

### 2. Get User by ID
Retrieve a specific user by their ID.

**Endpoint**: `GET /users/{id}`

**Parameters**:
- `id` (path) - User ID

**Response**: `200 OK` or `404 Not Found`
```json
{
  "id": 1,
  "username": "john.doe",
  "email": "john.doe@company.com",
  "fullName": "John Doe",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "groups": [
    {
      "id": 1,
      "name": "Engineering",
      "description": "Software Engineering Team",
      "active": true
    }
  ]
}
```

---

### 3. Get User by Username
Retrieve a user by their username.

**Endpoint**: `GET /users/username/{username}`

**Parameters**:
- `username` (path) - Username

**Response**: `200 OK` or `404 Not Found`

---

### 4. Get Active Users
Retrieve all active users.

**Endpoint**: `GET /users/active`

**Response**: `200 OK`

---

### 5. Create User
Create a new user in the system.

**Endpoint**: `POST /users`

**Request Body**:
```json
{
  "username": "jane.smith",
  "email": "jane.smith@company.com",
  "fullName": "Jane Smith",
  "active": true
}
```

**Validation Rules**:
- `username`: Required, must be unique
- `email`: Required, must be valid email format, must be unique
- `fullName`: Required
- `active`: Optional, defaults to true

**Response**: `201 Created`
```json
{
  "id": 2,
  "username": "jane.smith",
  "email": "jane.smith@company.com",
  "fullName": "Jane Smith",
  "active": true,
  "createdAt": "2024-01-15T11:00:00",
  "updatedAt": "2024-01-15T11:00:00",
  "groups": []
}
```

**Error Response**: `400 Bad Request`
```json
{
  "error": "Username already exists"
}
```

---

### 6. Update User
Update an existing user's information.

**Endpoint**: `PUT /users/{id}`

**Parameters**:
- `id` (path) - User ID

**Request Body**:
```json
{
  "username": "jane.smith.updated",
  "email": "jane.updated@company.com",
  "fullName": "Jane Smith Updated",
  "active": false
}
```

**Response**: `200 OK` or `400 Bad Request`

---

### 7. Delete User
Delete a user from the system.

**Endpoint**: `DELETE /users/{id}`

**Parameters**:
- `id` (path) - User ID

**Response**: `204 No Content` or `404 Not Found`

---

### 8. Add User to Group
Add a user to a specific group.

**Endpoint**: `POST /users/{userId}/groups/{groupId}`

**Parameters**:
- `userId` (path) - User ID
- `groupId` (path) - Group ID

**Response**: `200 OK`

---

### 9. Remove User from Group
Remove a user from a specific group.

**Endpoint**: `DELETE /users/{userId}/groups/{groupId}`

**Parameters**:
- `userId` (path) - User ID
- `groupId` (path) - Group ID

**Response**: `200 OK`

---

## Group Management APIs

### 1. Get All Groups
Retrieve all groups in the system.

**Endpoint**: `GET /groups`

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "name": "Engineering",
    "description": "Software Engineering Team",
    "active": true,
    "createdAt": "2024-01-15T09:00:00",
    "updatedAt": "2024-01-15T09:00:00"
  }
]
```

---

### 2. Get Group by ID
Retrieve a specific group by ID.

**Endpoint**: `GET /groups/{id}`

**Parameters**:
- `id` (path) - Group ID

**Response**: `200 OK` or `404 Not Found`

---

### 3. Get Group by Name
Retrieve a group by name.

**Endpoint**: `GET /groups/name/{name}`

**Parameters**:
- `name` (path) - Group name

**Response**: `200 OK` or `404 Not Found`

---

### 4. Get Active Groups
Retrieve all active groups.

**Endpoint**: `GET /groups/active`

**Response**: `200 OK`

---

### 5. Create Group
Create a new group.

**Endpoint**: `POST /groups`

**Request Body**:
```json
{
  "name": "Marketing",
  "description": "Marketing Department",
  "active": true
}
```

**Validation Rules**:
- `name`: Required, must be unique
- `description`: Optional
- `active`: Optional, defaults to true

**Response**: `201 Created`

---

### 6. Update Group
Update an existing group.

**Endpoint**: `PUT /groups/{id}`

**Parameters**:
- `id` (path) - Group ID

**Request Body**:
```json
{
  "name": "Marketing Team",
  "description": "Updated Marketing Department",
  "active": true
}
```

**Response**: `200 OK` or `400 Bad Request`

---

### 7. Delete Group
Delete a group.

**Endpoint**: `DELETE /groups/{id}`

**Parameters**:
- `id` (path) - Group ID

**Response**: `204 No Content` or `404 Not Found`

---

## License Management APIs

### 1. Get All Licenses
Retrieve all licenses in the system.

**Endpoint**: `GET /licenses`

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "softwareName": "Microsoft Office 365",
    "licenseKey": "OFFICE-2024-ENT-001",
    "totalSeats": 100,
    "usedSeats": 25,
    "expirationDate": "2025-12-31T23:59:59",
    "active": true,
    "description": "Enterprise Office Suite",
    "createdAt": "2024-01-10T08:00:00",
    "updatedAt": "2024-01-15T14:30:00"
  }
]
```

---

### 2. Get License by ID
Retrieve a specific license.

**Endpoint**: `GET /licenses/{id}`

**Parameters**:
- `id` (path) - License ID

**Response**: `200 OK` or `404 Not Found`

---

### 3. Get Active Licenses
Retrieve all active licenses.

**Endpoint**: `GET /licenses/active`

**Response**: `200 OK`

---

### 4. Get Available Licenses
Retrieve licenses that have available seats.

**Endpoint**: `GET /licenses/available`

**Response**: `200 OK`

---

### 5. Create License
Create a new license.

**Endpoint**: `POST /licenses`

**Request Body**:
```json
{
  "softwareName": "Adobe Creative Cloud",
  "licenseKey": "ADOBE-CC-2024-002",
  "totalSeats": 50,
  "expirationDate": "2025-06-30T23:59:59",
  "active": true,
  "description": "Creative Cloud All Apps"
}
```

**Validation Rules**:
- `softwareName`: Required
- `licenseKey`: Required, must be unique
- `totalSeats`: Required, must be positive number
- `expirationDate`: Optional
- `active`: Optional, defaults to true
- `description`: Optional

**Response**: `201 Created`

**Automatic Actions**:
- Creates LICENSE_CREATED history entry
- Sets usedSeats to 0

---

### 6. Update License
Update an existing license.

**Endpoint**: `PUT /licenses/{id}`

**Parameters**:
- `id` (path) - License ID

**Request Body**:
```json
{
  "softwareName": "Adobe Creative Cloud",
  "licenseKey": "ADOBE-CC-2024-002",
  "totalSeats": 75,
  "expirationDate": "2025-12-31T23:59:59",
  "active": true,
  "description": "Updated license"
}
```

**Response**: `200 OK`

**Automatic Actions**:
- Creates LICENSE_UPDATED history entry
- If seats changed: Creates SEATS_INCREASED or SEATS_DECREASED history entry

---

### 7. Delete License
Delete a license.

**Endpoint**: `DELETE /licenses/{id}`

**Parameters**:
- `id` (path) - License ID

**Response**: `204 No Content`

**Automatic Actions**:
- Creates LICENSE_DELETED history entry
- Cascades deletion to all assignments

---

## License Assignment APIs

### 1. Assign License to User
Assign a license to a specific user.

**Endpoint**: `POST /licenses/{licenseId}/assign/user/{userId}`

**Parameters**:
- `licenseId` (path) - License ID
- `userId` (path) - User ID

**Request Body** (optional):
```json
{
  "notes": "Assigned for project work"
}
```

**Response**: `201 Created`
```json
{
  "id": 1,
  "user": {
    "id": 1,
    "username": "john.doe",
    "fullName": "John Doe"
  },
  "license": {
    "id": 1,
    "softwareName": "Microsoft Office 365"
  },
  "assignedAt": "2024-01-15T15:00:00",
  "revokedAt": null,
  "active": true,
  "notes": "Assigned for project work"
}
```

**Business Rules**:
- User must exist
- License must exist
- License must have available seats
- User cannot already have this license assigned

**Automatic Actions**:
- Increments license usedSeats
- Creates LICENSE_ASSIGNED_TO_USER history entry

**Error Response**: `400 Bad Request`
```json
{
  "error": "No available seats for this license"
}
```

---

### 2. Revoke License from User
Revoke a license assignment from a user.

**Endpoint**: `DELETE /licenses/user-assignments/{userLicenseId}`

**Parameters**:
- `userLicenseId` (path) - UserLicense assignment ID

**Response**: `204 No Content`

**Automatic Actions**:
- Decrements license usedSeats
- Sets assignment inactive and revokedAt timestamp
- Creates LICENSE_REVOKED_FROM_USER history entry

---

### 3. Get User's Licenses
Get all active licenses assigned to a user.

**Endpoint**: `GET /licenses/user/{userId}`

**Parameters**:
- `userId` (path) - User ID

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "license": {
      "id": 1,
      "softwareName": "Microsoft Office 365",
      "licenseKey": "OFFICE-2024-ENT-001"
    },
    "assignedAt": "2024-01-15T15:00:00",
    "active": true,
    "notes": "Assigned for project work"
  }
]
```

---

### 4. Get License's Users
Get all users who have a specific license.

**Endpoint**: `GET /licenses/{licenseId}/users`

**Parameters**:
- `licenseId` (path) - License ID

**Response**: `200 OK`

---

### 5. Assign License to Group
Assign a license to a group with allocated seats.

**Endpoint**: `POST /licenses/{licenseId}/assign/group/{groupId}`

**Parameters**:
- `licenseId` (path) - License ID
- `groupId` (path) - Group ID

**Request Body**:
```json
{
  "allocatedSeats": 25,
  "notes": "Allocated for Engineering Department"
}
```

**Validation Rules**:
- `allocatedSeats`: Required, must be positive
- `notes`: Optional

**Response**: `201 Created`

**Business Rules**:
- Group must exist
- License must exist
- License must have enough available seats
- Group cannot already have this license assigned

**Automatic Actions**:
- Increases license usedSeats by allocatedSeats
- Creates LICENSE_ASSIGNED_TO_GROUP history entry

---

### 6. Revoke License from Group
Revoke a license assignment from a group.

**Endpoint**: `DELETE /licenses/group-assignments/{groupLicenseId}`

**Parameters**:
- `groupLicenseId` (path) - GroupLicense assignment ID

**Response**: `204 No Content`

**Automatic Actions**:
- Decreases license usedSeats by allocatedSeats
- Sets assignment inactive and revokedAt timestamp
- Creates LICENSE_REVOKED_FROM_GROUP history entry

---

### 7. Get Group's Licenses
Get all active licenses assigned to a group.

**Endpoint**: `GET /licenses/group/{groupId}`

**Parameters**:
- `groupId` (path) - Group ID

**Response**: `200 OK`

---

### 8. Get License's Groups
Get all groups that have a specific license.

**Endpoint**: `GET /licenses/{licenseId}/groups`

**Parameters**:
- `licenseId` (path) - License ID

**Response**: `200 OK`

---

## Audit & History APIs

### 1. Get License History
Get complete audit trail for a specific license.

**Endpoint**: `GET /licenses/{licenseId}/history`

**Parameters**:
- `licenseId` (path) - License ID

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "licenseId": 1,
    "userId": null,
    "groupId": null,
    "actionType": "LICENSE_CREATED",
    "description": "License created: Microsoft Office 365",
    "details": "Total seats: 100, Expiration: 2025-12-31",
    "timestamp": "2024-01-10T08:00:00",
    "performedBy": "system"
  },
  {
    "id": 5,
    "licenseId": 1,
    "userId": 1,
    "groupId": null,
    "actionType": "LICENSE_ASSIGNED_TO_USER",
    "description": "License assigned to user: john.doe",
    "details": "Assigned for project work",
    "timestamp": "2024-01-15T15:00:00",
    "performedBy": "system"
  }
]
```

---

### 2. Get User History
Get audit trail for all license operations involving a specific user.

**Endpoint**: `GET /licenses/history/user/{userId}`

**Parameters**:
- `userId` (path) - User ID

**Response**: `200 OK`

---

### 3. Get Recent History
Get the 50 most recent license operations.

**Endpoint**: `GET /licenses/history/recent`

**Response**: `200 OK`

---

## Error Handling

### HTTP Status Codes

| Code | Meaning | When Used |
|------|---------|-----------|
| 200 | OK | Successful GET, PUT requests |
| 201 | Created | Successful POST requests |
| 204 | No Content | Successful DELETE requests |
| 400 | Bad Request | Validation errors, business rule violations |
| 404 | Not Found | Resource doesn't exist |
| 500 | Internal Server Error | Unexpected server errors |

### Error Response Format

```json
{
  "timestamp": "2024-01-15T16:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Username already exists",
  "path": "/api/users"
}
```

### Common Error Messages

| Message | Cause | Solution |
|---------|-------|----------|
| "Username already exists" | Duplicate username | Use a different username |
| "Email already exists" | Duplicate email | Use a different email |
| "License key already exists" | Duplicate license key | Use a different key |
| "No available seats" | License fully utilized | Increase total seats or revoke assignments |
| "User not found" | Invalid user ID | Check user ID |
| "License already assigned" | Duplicate assignment | User/group already has license |

---

## Rate Limiting

Currently, there are no rate limits on API endpoints. This may change in production deployments.

---

## Authentication & Authorization

**Current Implementation**: No authentication required (development mode)

**Future Implementation**: 
- JWT-based authentication
- Role-based access control (RBAC)
- API key support

---

## Pagination

**Current Implementation**: No pagination (all results returned)

**Future Implementation**:
- Page-based pagination with `?page=0&size=20`
- Total count in response headers

---

## Best Practices

1. **Always check available seats** before assigning licenses
2. **Use history endpoints** for audit compliance
3. **Handle 404 errors** gracefully in client applications
4. **Validate data** before sending requests
5. **Use bulk operations** when assigning to multiple users (future feature)

---

For questions or issues, please refer to the [README.md](README.md) or create an issue in the repository.
