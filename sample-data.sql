-- Sample Data for Testing
-- This file can be used to populate the database with test data

-- Insert Sample Users
INSERT INTO users (username, email, full_name, active, created_at, updated_at) VALUES
('john.doe', 'john.doe@company.com', 'John Doe', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jane.smith', 'jane.smith@company.com', 'Jane Smith', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('bob.johnson', 'bob.johnson@company.com', 'Bob Johnson', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('alice.williams', 'alice.williams@company.com', 'Alice Williams', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('charlie.brown', 'charlie.brown@company.com', 'Charlie Brown', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Sample Groups
INSERT INTO user_groups (name, description, active, created_at, updated_at) VALUES
('Engineering', 'Software Engineering Team', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Marketing', 'Marketing Department', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sales', 'Sales Team', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HR', 'Human Resources', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Management', 'Management Team', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Sample Licenses
INSERT INTO licenses (software_name, license_key, total_seats, used_seats, expiration_date, active, description, created_at, updated_at) VALUES
('Microsoft Office 365', 'OFFICE-2024-ENT-001', 100, 0, '2025-12-31', true, 'Enterprise Office Suite', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Adobe Creative Cloud', 'ADOBE-CC-2024-002', 50, 0, '2025-06-30', true, 'Creative Cloud All Apps', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('JetBrains IntelliJ IDEA', 'JETBRAINS-IJ-003', 30, 0, '2025-12-31', true, 'Ultimate Edition', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Slack Business', 'SLACK-BUS-2024-004', 200, 0, '2025-12-31', true, 'Business Communication', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GitHub Enterprise', 'GITHUB-ENT-005', 75, 0, '2025-12-31', true, 'Enterprise Development Platform', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Map Users to Groups (Many-to-Many)
-- John Doe in Engineering and Management
INSERT INTO user_group_mapping (user_id, group_id) VALUES
(1, 1), (1, 5);

-- Jane Smith in Marketing
INSERT INTO user_group_mapping (user_id, group_id) VALUES
(2, 2);

-- Bob Johnson in Engineering
INSERT INTO user_group_mapping (user_id, group_id) VALUES
(3, 1);

-- Alice Williams in Sales
INSERT INTO user_group_mapping (user_id, group_id) VALUES
(4, 3);

-- Charlie Brown in HR and Management
INSERT INTO user_group_mapping (user_id, group_id) VALUES
(5, 4), (5, 5);

-- Sample License Assignments
-- Assign Office 365 to John Doe
INSERT INTO user_licenses (user_id, license_id, assigned_at, active, notes) VALUES
(1, 1, CURRENT_TIMESTAMP, true, 'Standard deployment');

-- Assign IntelliJ to Engineering Group (20 seats)
INSERT INTO group_licenses (group_id, license_id, allocated_seats, assigned_at, active, notes) VALUES
(1, 3, 20, CURRENT_TIMESTAMP, true, 'For software development team');

-- Assign Slack to all users
INSERT INTO user_licenses (user_id, license_id, assigned_at, active) VALUES
(1, 4, CURRENT_TIMESTAMP, true),
(2, 4, CURRENT_TIMESTAMP, true),
(3, 4, CURRENT_TIMESTAMP, true),
(4, 4, CURRENT_TIMESTAMP, true),
(5, 4, CURRENT_TIMESTAMP, true);

-- Update used seats count
UPDATE licenses SET used_seats = 1 WHERE id = 1;  -- Office 365
UPDATE licenses SET used_seats = 20 WHERE id = 3; -- IntelliJ
UPDATE licenses SET used_seats = 5 WHERE id = 4;  -- Slack

-- Sample History Entries
INSERT INTO license_history (license_id, action_type, description, timestamp, performed_by) VALUES
(1, 'LICENSE_CREATED', 'License created: Microsoft Office 365', CURRENT_TIMESTAMP, 'system'),
(2, 'LICENSE_CREATED', 'License created: Adobe Creative Cloud', CURRENT_TIMESTAMP, 'system'),
(3, 'LICENSE_CREATED', 'License created: JetBrains IntelliJ IDEA', CURRENT_TIMESTAMP, 'system'),
(4, 'LICENSE_CREATED', 'License created: Slack Business', CURRENT_TIMESTAMP, 'system'),
(5, 'LICENSE_CREATED', 'License created: GitHub Enterprise', CURRENT_TIMESTAMP, 'system');
