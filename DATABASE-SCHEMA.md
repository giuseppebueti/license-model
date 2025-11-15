# Database Schema Documentation

## Entity Relationship Overview

```
┌─────────────┐         ┌──────────────────┐         ┌─────────────┐
│   USERS     │────────▶│USER_GROUP_MAPPING│◀────────│ USER_GROUPS │
└─────────────┘         └──────────────────┘         └─────────────┘
      │                                                      │
      │                                                      │
      ▼                                                      ▼
┌──────────────┐                                    ┌───────────────┐
│USER_LICENSES │                                    │GROUP_LICENSES │
└──────────────┘                                    └───────────────┘
      │                                                      │
      │              ┌──────────┐                           │
      └─────────────▶│ LICENSES │◀──────────────────────────┘
                     └──────────┘
                           │
                           ▼
                   ┌────────────────┐
                   │LICENSE_HISTORY │
                   └────────────────┘
```

## Tables

### 1. USERS
Stores user information.

| Column     | Type         | Constraints           | Description                    |
|------------|--------------|----------------------|--------------------------------|
| id         | BIGINT       | PRIMARY KEY, AUTO    | Unique identifier              |
| username   | VARCHAR(255) | UNIQUE, NOT NULL     | User's login name              |
| email      | VARCHAR(255) | UNIQUE, NOT NULL     | User's email address           |
| full_name  | VARCHAR(255) | NOT NULL             | User's full name               |
| active     | BOOLEAN      | NOT NULL, DEFAULT true| Active status                  |
| created_at | TIMESTAMP    | NOT NULL             | Creation timestamp             |
| updated_at | TIMESTAMP    | NOT NULL             | Last update timestamp          |

**Relationships:**
- Many-to-Many with USER_GROUPS via USER_GROUP_MAPPING
- One-to-Many with USER_LICENSES

---

### 2. USER_GROUPS
Stores group/department information.

| Column      | Type         | Constraints           | Description                    |
|-------------|--------------|----------------------|--------------------------------|
| id          | BIGINT       | PRIMARY KEY, AUTO    | Unique identifier              |
| name        | VARCHAR(255) | UNIQUE, NOT NULL     | Group name                     |
| description | VARCHAR(500) | NULLABLE             | Group description              |
| active      | BOOLEAN      | NOT NULL, DEFAULT true| Active status                 |
| created_at  | TIMESTAMP    | NOT NULL             | Creation timestamp             |
| updated_at  | TIMESTAMP    | NOT NULL             | Last update timestamp          |

**Relationships:**
- Many-to-Many with USERS via USER_GROUP_MAPPING
- One-to-Many with GROUP_LICENSES

---

### 3. USER_GROUP_MAPPING
Junction table for Users and Groups (Many-to-Many).

| Column   | Type   | Constraints     | Description           |
|----------|--------|-----------------|-----------------------|
| user_id  | BIGINT | FOREIGN KEY     | Reference to USERS    |
| group_id | BIGINT | FOREIGN KEY     | Reference to USER_GROUPS |

**Indexes:**
- PRIMARY KEY (user_id, group_id)
- INDEX on user_id
- INDEX on group_id

---

### 4. LICENSES
Stores software license information.

| Column          | Type         | Constraints           | Description                    |
|-----------------|--------------|----------------------|--------------------------------|
| id              | BIGINT       | PRIMARY KEY, AUTO    | Unique identifier              |
| software_name   | VARCHAR(255) | NOT NULL             | Software product name          |
| license_key     | VARCHAR(255) | UNIQUE, NOT NULL     | Unique license key             |
| total_seats     | INTEGER      | NOT NULL             | Total available seats          |
| used_seats      | INTEGER      | NOT NULL, DEFAULT 0  | Currently used seats           |
| expiration_date | TIMESTAMP    | NULLABLE             | License expiration date        |
| active          | BOOLEAN      | NOT NULL, DEFAULT true| Active status                 |
| description     | VARCHAR(500) | NULLABLE             | License description            |
| created_at      | TIMESTAMP    | NOT NULL             | Creation timestamp             |
| updated_at      | TIMESTAMP    | NOT NULL             | Last update timestamp          |

**Computed Fields:**
- available_seats = total_seats - used_seats

**Relationships:**
- One-to-Many with USER_LICENSES
- One-to-Many with GROUP_LICENSES
- One-to-Many with LICENSE_HISTORY

---

### 5. USER_LICENSES
Junction table for User-License assignments.

| Column      | Type         | Constraints           | Description                    |
|-------------|--------------|----------------------|--------------------------------|
| id          | BIGINT       | PRIMARY KEY, AUTO    | Unique identifier              |
| user_id     | BIGINT       | FOREIGN KEY, NOT NULL| Reference to USERS             |
| license_id  | BIGINT       | FOREIGN KEY, NOT NULL| Reference to LICENSES          |
| assigned_at | TIMESTAMP    | NOT NULL             | Assignment timestamp           |
| revoked_at  | TIMESTAMP    | NULLABLE             | Revocation timestamp           |
| active      | BOOLEAN      | NOT NULL, DEFAULT true| Active status                 |
| notes       | VARCHAR(500) | NULLABLE             | Assignment notes               |

**Indexes:**
- INDEX on user_id
- INDEX on license_id
- INDEX on (user_id, license_id, active)

---

### 6. GROUP_LICENSES
Junction table for Group-License assignments.

| Column          | Type         | Constraints           | Description                    |
|-----------------|--------------|----------------------|--------------------------------|
| id              | BIGINT       | PRIMARY KEY, AUTO    | Unique identifier              |
| group_id        | BIGINT       | FOREIGN KEY, NOT NULL| Reference to USER_GROUPS       |
| license_id      | BIGINT       | FOREIGN KEY, NOT NULL| Reference to LICENSES          |
| allocated_seats | INTEGER      | NOT NULL             | Seats allocated to group       |
| assigned_at     | TIMESTAMP    | NOT NULL             | Assignment timestamp           |
| revoked_at      | TIMESTAMP    | NULLABLE             | Revocation timestamp           |
| active          | BOOLEAN      | NOT NULL, DEFAULT true| Active status                 |
| notes           | VARCHAR(500) | NULLABLE             | Assignment notes               |

**Indexes:**
- INDEX on group_id
- INDEX on license_id
- INDEX on (group_id, license_id, active)

---

### 7. LICENSE_HISTORY
Audit trail for all license operations.

| Column       | Type          | Constraints           | Description                    |
|--------------|---------------|----------------------|--------------------------------|
| id           | BIGINT        | PRIMARY KEY, AUTO    | Unique identifier              |
| license_id   | BIGINT        | NOT NULL             | Reference to LICENSES          |
| user_id      | BIGINT        | NULLABLE             | Reference to USERS (if applicable)|
| group_id     | BIGINT        | NULLABLE             | Reference to USER_GROUPS (if applicable)|
| action_type  | VARCHAR(50)   | NOT NULL             | Type of action performed       |
| description  | VARCHAR(1000) | NOT NULL             | Action description             |
| details      | VARCHAR(2000) | NULLABLE             | Additional details             |
| timestamp    | TIMESTAMP     | NOT NULL             | When action occurred           |
| performed_by | VARCHAR(255)  | NULLABLE             | Who performed the action       |

**Action Types (ENUM):**
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

**Indexes:**
- INDEX on license_id
- INDEX on user_id
- INDEX on group_id
- INDEX on timestamp
- INDEX on action_type

---

## Key Relationships Summary

### Many-to-Many Relationships

1. **Users ↔ Groups**
   - Via: USER_GROUP_MAPPING
   - Purpose: A user can belong to multiple groups (e.g., a teacher in multiple departments)
   - Example: John Doe is in both "Engineering" and "Management" groups

2. **Users ↔ Licenses**
   - Via: USER_LICENSES
   - Purpose: Track individual license assignments
   - Example: Jane Smith assigned Microsoft Office license

3. **Groups ↔ Licenses**
   - Via: GROUP_LICENSES
   - Purpose: Allocate seats to entire groups
   - Example: "Engineering" group allocated 25 seats of IntelliJ IDEA

### Seat Management Logic

```
For each License:
  total_seats = Maximum available seats
  used_seats = SUM(active user assignments) + SUM(active group allocations)
  available_seats = total_seats - used_seats
```

### Audit Trail

Every license operation is automatically logged in LICENSE_HISTORY:
- Who performed the action (performed_by)
- What was done (action_type)
- When it happened (timestamp)
- Which entities were involved (license_id, user_id, group_id)
- Additional context (description, details)

---

## Data Integrity Rules

1. **Username uniqueness**: No duplicate usernames
2. **Email uniqueness**: No duplicate emails
3. **License key uniqueness**: No duplicate license keys
4. **Group name uniqueness**: No duplicate group names
5. **Seat validation**: Cannot assign more seats than available
6. **Cascade deletes**: Deleting a user/group also removes their license assignments
7. **Soft deletes**: License assignments marked inactive rather than deleted
8. **Timestamp tracking**: All entities track creation and update times

---

## Query Performance Considerations

### Recommended Indexes

All foreign keys are automatically indexed for optimal JOIN performance:
- user_id in USER_LICENSES
- group_id in GROUP_LICENSES
- license_id in USER_LICENSES and GROUP_LICENSES
- Composite indexes on (user_id, license_id, active)

### Common Query Patterns

1. **Find all licenses for a user**
   ```sql
   SELECT l.* FROM licenses l
   JOIN user_licenses ul ON l.id = ul.license_id
   WHERE ul.user_id = ? AND ul.active = true
   ```

2. **Find available licenses**
   ```sql
   SELECT * FROM licenses
   WHERE used_seats < total_seats AND active = true
   ```

3. **Audit trail for a license**
   ```sql
   SELECT * FROM license_history
   WHERE license_id = ?
   ORDER BY timestamp DESC
   ```

---

## Migration Notes

When upgrading from previous versions:

1. **Many-to-One to Many-to-Many conversion** (Users ↔ Groups)
   - Create USER_GROUP_MAPPING table
   - Migrate existing relationships
   - Drop old foreign key column

2. **Adding audit trail**
   - Create LICENSE_HISTORY table
   - Backfill historical data if available
   - Set up triggers for automatic logging

See `MIGRATION-GUIDE.md` for detailed migration scripts.
