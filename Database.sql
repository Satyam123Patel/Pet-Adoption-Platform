create DATABASE pet_adoption_and_tracking_management_system;
USE pet_adoption_and_tracking_management_system;

-- Users Table (for authentication)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- Admins Table
CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'ADMIN',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email)
);

-- Shelters Table
CREATE TABLE shelters (
    shelter_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Main Pets Table (approved pets for adoption)
CREATE TABLE pet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    breed VARCHAR(50),
    age INT,
    gender CHAR(1) DEFAULT 'U',
    status VARCHAR(20) DEFAULT 'available',
    image_url VARCHAR(255),
    shelter_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_category (category)
);

-- Pending Pets Table (user submissions awaiting approval)
CREATE TABLE pets_pending (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    breed VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    location VARCHAR(100),
    description TEXT,
    phone VARCHAR(20),
    image_path VARCHAR(255),
    status VARCHAR(20) DEFAULT 'pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_email (email)
);

select database();

-- DROP TRIGGER IF EXISTS trg_update_pet_status_after_request;
-- DROP TRIGGER IF EXISTS trg_set_decision_date;
-- DROP PROCEDURE IF EXISTS submit_adoption_request;
-- DROP PROCEDURE IF EXISTS approve_adoption_request;
-- DROP PROCEDURE IF EXISTS reject_adoption_request;
-- DROP PROCEDURE IF EXISTS get_pending_requests;
-- drop table adoption_requests;

-- Adoption Requests Table
CREATE TABLE adoption_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,

    living_situation TEXT,
    previous_experience TEXT,
    family_composition TEXT,

    status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_adoption_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_adoption_pet
        FOREIGN KEY (pet_id) REFERENCES pet(id) ON DELETE CASCADE,

    INDEX idx_user_id (user_id),
    INDEX idx_pet_id (pet_id),
    INDEX idx_status (status)
);


-- OTP Verification Table
CREATE TABLE otp_verification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    otp VARCHAR(10) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expiry_time DATETIME NOT NULL,
    verified BOOLEAN DEFAULT FALSE,
    INDEX idx_email (email)
);

-- Tracking Table (for adopted pets)
CREATE TABLE tracking (
    track_id INT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    location VARCHAR(255),
    note TEXT,
    vet_visit_date DATE,
    vaccinated BOOLEAN DEFAULT FALSE,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pet(id) ON DELETE CASCADE,
    INDEX idx_pet_id (pet_id)
);

-- ========================================
-- 3. INSERT INITIAL DATA
-- ========================================

-- Insert shelters
INSERT INTO shelters (name, address, phone, email) VALUES
('Happy Paws Shelter', 'Plot 21, Green Street, Mumbai', '9876543210', 'contact@happypaws.org'),
('Animal Care Center', 'Sec 12, Baner, Pune', '9988776655', 'support@animalcare.in'),
('Rescue & Love Foundation', 'Park Road, Nagpur', '9090909090', 'info@rescueandlove.org');

-- Insert admin (password: admin123)
-- Insert admin (password: admin123)
INSERT INTO admins (name, email, password_hash, role, created_at)
VALUES (
    'System Admin',
    'satyampatelkatni2003@gmail.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ADMIN',
    NOW()
);

-- Insert test users in users table (password: admin123)
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'satyampatelkatni2003@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
('Test User', 'user@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER');

-- Insert sample pets
INSERT INTO pet (name, category, breed, age, gender, status, image_url, shelter_id) VALUES
('Bruno', 'dog', 'Labrador', 3, 'M', 'available', '1718906386213-550689756.jpg', 1),
('Lucy', 'dog', 'Beagle', 2, 'F', 'available', '1718906531802-606585739.jpg', 1),
('Mia', 'cat', 'Persian', 1, 'F', 'available', '1718906182775-8078205.jpeg', 2),
('Oscar', 'cat', 'Siamese', 5, 'M', 'available', '1718906085691-433161642.jpeg', 2),
('Rocky', 'dog', 'Golden Retriever', 3, 'M', 'available', '1718906473944-923464963.jpg', 3),
('Ginger', 'dog', 'Pug', 6, 'F', 'available', '1718906609733-573010075.jpg', 3),
('Chirpy', 'bird', 'Parrot', 2, 'U', 'available', '1718907085550-787999544.jpg', 2),
('Goldy', 'fish', 'Goldfish', 1, 'M', 'available', '1718906809727-756049182.jpg', 1),
('Nemo', 'fish', 'Clown', 1, 'M', 'available', '1718906865077-840488064.jpg', 1),
('White', 'rabbit', 'Normal', 1, 'F', 'available', '1718906609733-573010075.jpg', 3);

-- ========================================
-- 4. TRIGGERS
-- ========================================

-- Trigger: Update pet status when adoption request is updated
DELIMITER $$

CREATE TRIGGER trg_update_pet_status_after_request
AFTER UPDATE ON adoption_requests
FOR EACH ROW
BEGIN
    IF NEW.status = 'APPROVED' THEN
        UPDATE pet
        SET status = 'adopted'
        WHERE id = NEW.pet_id;

    ELSEIF NEW.status = 'REJECTED' THEN
        UPDATE pet
        SET status = 'available'
        WHERE id = NEW.pet_id;

    ELSEIF NEW.status = 'PENDING' THEN
        UPDATE pet
        SET status = 'pending'
        WHERE id = NEW.pet_id;
    END IF;
END$$

DELIMITER ;


-- Trigger: Update timestamp  when adoption request is approved/rejected
DELIMITER $$

CREATE TRIGGER trg_set_decision_date
BEFORE UPDATE ON adoption_requests
FOR EACH ROW
BEGIN
    IF NEW.status IN ('APPROVED', 'REJECTED')
       AND OLD.status = 'PENDING' THEN
        SET NEW.updated_at = NOW();
    END IF;
END$$

DELIMITER ;

-- ========================================
-- 5. STORED PROCEDURES
-- ========================================

-- Procedure: Submit adoption request
DELIMITER $$

CREATE PROCEDURE submit_adoption_request(
    IN p_user_id BIGINT,
    IN p_pet_id BIGINT,
    IN p_living_situation TEXT,
    IN p_previous_experience TEXT,
    IN p_family_composition TEXT
)
BEGIN
    INSERT INTO adoption_requests (
        user_id,
        pet_id,
        living_situation,
        previous_experience,
        family_composition,
        status
    )
    VALUES (
        p_user_id,
        p_pet_id,
        p_living_situation,
        p_previous_experience,
        p_family_composition,
        'PENDING'
    );

    -- Mark pet as pending
    UPDATE pet
    SET status = 'pending'
    WHERE id = p_pet_id;
END$$

DELIMITER ;


-- Procedure: Approve adoption request

DELIMITER $$

CREATE PROCEDURE approve_adoption_request(
    IN p_request_id BIGINT
)
BEGIN
    UPDATE adoption_requests
    SET status = 'APPROVED'
    WHERE id = p_request_id;
END$$

DELIMITER ;


-- Procedure: Reject adoption request

DELIMITER $$

CREATE PROCEDURE reject_adoption_request(
    IN p_request_id BIGINT
)
BEGIN
    UPDATE adoption_requests
    SET status = 'REJECTED'
    WHERE id = p_request_id;
END$$

DELIMITER ;

-- Procedure: Get all pending requests

DELIMITER $$

CREATE PROCEDURE get_pending_requests()
BEGIN
    SELECT
        ar.id AS request_id,
        ar.pet_id,
        p.name AS pet_name,
        u.name AS user_name,
        u.email AS user_email,
        ar.status,
        ar.created_at
    FROM adoption_requests ar
    JOIN pet p ON ar.pet_id = p.id
    JOIN users u ON ar.user_id = u.id
    WHERE ar.status = 'PENDING'
    ORDER BY ar.created_at ASC;
END$$

DELIMITER ;

SHOW TRIGGERS;
SHOW PROCEDURE STATUS
WHERE Db = 'pet_adoption_and_tracking_management_system';

-- ========================================
-- 6. VERIFICATION
-- ========================================

-- Show all tables
SHOW TABLES;

-- Show all triggers
SHOW TRIGGERS;

-- Show all procedures
SHOW PROCEDURE STATUS WHERE Db = 'pet_adoption_and_tracking_management_system';

-- Verify data counts
SELECT 'Shelters:' AS Table_Name, COUNT(*) AS Count FROM shelters
UNION ALL SELECT 'Admins:', COUNT(*) FROM admins
UNION ALL SELECT 'Users:', COUNT(*) FROM users
UNION ALL SELECT 'Pets:', COUNT(*) FROM pet
UNION ALL SELECT 'Pending Pets:', COUNT(*) FROM pets_pending
UNION ALL SELECT 'Adoption Requests:', COUNT(*) FROM adoption_requests
UNION ALL SELECT 'Tracking:', COUNT(*) FROM tracking;


-- Insert test tracking record for an adopted pet
INSERT INTO tracking (pet_id, location, note, vet_visit_date, vaccinated, updated_at)
VALUES (1, 'Mumbai', 'General health check-up', '2026-01-20', TRUE, NOW());

-- Verify
SELECT * FROM tracking;

SELECT COUNT(*) FROM pets_pending WHERE status = 'pending';
SELECT COUNT(*) FROM adoption_requests WHERE status = 'PENDING';
SELECT COUNT(*) FROM pet WHERE status = 'available';


SELECT COUNT(*) FROM adoption_requests WHERE status = 'PENDING';

-- View all tracking records
SELECT * FROM tracking ORDER BY updated_at DESC;

-- View tracking for specific pet
SELECT 
    t.*,
    p.name as pet_name,
    p.category,
    p.breed
FROM tracking t
JOIN pet p ON t.pet_id = p.id
WHERE t.pet_id = 1;

-- Count tracking records per pet
SELECT 
    p.name,
    COUNT(t.track_id) as tracking_count
FROM pet p
LEFT JOIN tracking t ON p.id = t.pet_id
WHERE p.status = 'adopted'
GROUP BY p.id, p.name;

-- Get vaccination statistics
SELECT 
    COUNT(*) as total_records,
    SUM(CASE WHEN vaccinated = TRUE THEN 1 ELSE 0 END) as vaccinated_count,
    SUM(CASE WHEN vaccinated = FALSE THEN 1 ELSE 0 END) as not_vaccinated_count
FROM tracking;

-- Recent tracking activities
SELECT 
    t.track_id,
    p.name as pet_name,
    t.location,
    t.note,
    t.vaccinated,
    t.updated_at
FROM tracking t
JOIN pet p ON t.pet_id = p.id
ORDER BY t.updated_at DESC
LIMIT 10;

-- How many pending submissions?
SELECT COUNT(*) FROM pets_pending WHERE status = 'pending';

-- How many adoption requests?
SELECT COUNT(*) FROM adoption_requests WHERE status = 'pending';

-- How many available pets?
SELECT COUNT(*) FROM pet WHERE status = 'available';

-- During admin operations:
SELECT * FROM pets_pending WHERE status = 'pending';
SELECT * FROM adoption_requests WHERE status = 'pending';

-- To check data:
SELECT * FROM pet;
SELECT * FROM users;

-- To get stats:
SELECT status, COUNT(*) FROM pet GROUP BY status;
SELECT status, COUNT(*) FROM pets_pending GROUP BY status;

-- Show system overview
SELECT 
    (SELECT COUNT(*) FROM users) as total_users,
    (SELECT COUNT(*) FROM pet WHERE status = 'available') as available_pets,
    (SELECT COUNT(*) FROM adoption_requests WHERE status = 'pending') as pending_requests;

-- Show recent activity
SELECT * FROM pet ORDER BY created_at DESC LIMIT 5;
SELECT * FROM pets_pending ORDER BY created_at DESC LIMIT 5;

-- Monitoring by these queries ===========================

-- View all pets
SELECT * FROM pet;

-- View all pending pet submissions
SELECT * FROM pets_pending;

-- View all adoption requests
SELECT * FROM adoption_requests;

-- View all users
SELECT * FROM users;

-- View all admins
SELECT * FROM admins;

-- View all shelters
SELECT * FROM shelters;

-- ========================================
-- 2. DETAILED PET INFORMATION
-- ========================================

-- All pets with details
SELECT 
    id AS pet_id,
    name,
    breed,
    age,
    gender,
    status,
    image_url,
    shelter_id,
    created_at
FROM pet
ORDER BY created_at DESC;

-- Available pets only
SELECT * FROM pet WHERE status = 'available';

-- Pending pets only
SELECT * FROM pet WHERE status = 'pending';

-- Adopted pets only
SELECT * FROM pet WHERE status = 'adopted';

-- Count pets by status
SELECT 
    status, 
    COUNT(*) as count 
FROM pet
GROUP BY status;

-- Count pets by category (requires adding category column)
-- If you don't have category in pets table, skip this
-- SELECT category, COUNT(*) as count FROM pets GROUP BY category;

-- ========================================
-- 3. PENDING PET SUBMISSIONS (User Posted Pets)
-- ========================================

-- All pending submissions
SELECT * FROM pets_pending ORDER BY created_at DESC;

-- Only pending (not approved/rejected)
SELECT 
    id,
    name as owner_name,
    email,
    category,
    breed,
    age,
    gender,
    location,
    phone,
    status,
    image_path,
    created_at
FROM pets_pending 
WHERE status = 'pending'
ORDER BY created_at DESC;

-- Approved submissions
SELECT * FROM pets_pending WHERE status = 'approved';

-- Rejected submissions
SELECT * FROM pets_pending WHERE status = 'rejected';

-- Count pending submissions by status
SELECT 
    status, 
    COUNT(*) as count 
FROM pets_pending 
GROUP BY status;

-- Recent submissions (last 24 hours)
SELECT * FROM pets_pending 
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 24 HOUR)
ORDER BY created_at DESC;

-- ========================================
-- 4. ADOPTION REQUESTS
-- ========================================

-- All adoption requests
SELECT 
    id,
    user_id,
    pet_id,
    status,
    created_at,
    updated_at
FROM adoption_requests
WHERE status = 'PENDING'
ORDER BY created_at DESC;

--  performing join operations of admin with user and pet details
SELECT 
    ar.id AS request_id,
    u.id AS user_id,
    u.name AS user_name,
    u.email AS user_email,
    p.id AS pet_id,
    p.name AS pet_name,
    p.breed,
    p.age,
    ar.status,
    ar.created_at
FROM adoption_requests ar
JOIN users u ON ar.user_id = u.id
JOIN pet p ON ar.pet_id = p.id
ORDER BY ar.created_at DESC;


-- Approved requests
SELECT * FROM adoption_requests WHERE status = 'approved';

-- Rejected requests
SELECT * FROM adoption_requests WHERE status = 'rejected';

-- Count requests by status
SELECT 
    status, 
    COUNT(*) as count 
FROM adoption_requests 
GROUP BY status;

-- ========================================
-- 5. ADMIN & USER INFORMATION
-- ========================================

-- All admins
SELECT 
    admin_id,
    name,
    email,
    role,
    created_at
FROM admins;

-- All users
SELECT 
    id,
    name,
    email,
    role,
    created_at
FROM users
ORDER BY created_at DESC;

-- Users by role
SELECT role, COUNT(*) as count FROM users GROUP BY role;

-- Recent user signups (last 7 days)
SELECT * FROM users 
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
ORDER BY created_at DESC;

-- ========================================
-- 6. SHELTER INFORMATION
-- ========================================

-- All shelters
SELECT * FROM shelters;

-- Pets per shelter
SELECT 
    s.shelter_id,
    s.name AS shelter_name,
    COUNT(p.id) AS pet_count
FROM shelters s
LEFT JOIN pet p ON s.shelter_id = p.shelter_id
GROUP BY s.shelter_id, s.name;


-- ========================================
-- 7. COMBINED QUERIES (Detailed Views)
-- ========================================

-- Adoption requests with pet details
SELECT 
    ar.id AS request_id,
    ar.pet_id,
    p.name AS pet_name,
    p.breed AS pet_breed,
    p.age AS pet_age,
    ar.status,
    ar.created_at,
    ar.updated_at
FROM adoption_requests ar
JOIN pet p ON ar.pet_id = p.id
ORDER BY ar.created_at DESC;

-- Adoption requests with pet+shleter+user

SELECT 
    ar.id AS request_id,

    u.id AS user_id,
    u.name AS user_name,
    u.email AS user_email,

    p.id AS pet_id,
    p.name AS pet_name,
    p.breed,
    p.age,
    p.status AS pet_status,

    s.shelter_id,
    s.name AS shelter_name,
    s.address AS shelter_address,

    ar.status AS request_status,
    ar.created_at
FROM adoption_requests ar
JOIN users u ON ar.user_id = u.id
JOIN pet p ON ar.pet_id = p.id
LEFT JOIN shelters s ON p.shelter_id = s.shelter_id
ORDER BY ar.created_at DESC;


-- Pets with shelter information
SELECT 
    p.id AS pet_id,
    p.name AS pet_name,
    p.breed,
    p.age,
    p.status,
    s.name AS shelter_name,
    s.address AS shelter_address
FROM pet p
LEFT JOIN shelters s ON p.shelter_id = s.shelter_id
ORDER BY p.created_at DESC;


-- Pending submissions with complete details
SELECT 
    pp.id,
    pp.name as owner_name,
    pp.email as owner_email,
    pp.phone as owner_phone,
    pp.category as pet_category,
    pp.breed as pet_breed,
    pp.age as pet_age,
    pp.gender as pet_gender,
    pp.location,
    pp.description as reason,
    pp.status,
    pp.image_path,
    pp.created_at
FROM pets_pending pp
WHERE pp.status = 'pending'
ORDER BY pp.created_at DESC;

-- ========================================
-- 8. STATISTICS & ANALYTICS
-- ========================================

-- Overall system stats
SELECT 
    (SELECT COUNT(*) FROM users) as total_users,
    (SELECT COUNT(*) FROM pet WHERE status = 'available') as available_pets,
    (SELECT COUNT(*) FROM adoption_requests WHERE status = 'pending') as pending_requests,
    (SELECT COUNT(*) FROM adoption_requests WHERE status = 'approved') as total_adoptions,
    (SELECT COUNT(*) FROM pets_pending WHERE status = 'pending') as pending_submissions;

-- Daily activity (last 7 days)
SELECT 
    DATE(created_at) as date,
    COUNT(*) as new_pets
FROM pet
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
GROUP BY DATE(created_at)
ORDER BY date DESC;

-- Monthly adoption stats
SELECT 
    DATE_FORMAT(created_at, '%Y-%m') AS month,
    COUNT(*) AS total_requests,
    SUM(CASE WHEN status = 'APPROVED' THEN 1 ELSE 0 END) AS approved,
    SUM(CASE WHEN status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected
FROM adoption_requests
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 6 MONTH)
GROUP BY DATE_FORMAT(created_at, '%Y-%m')
ORDER BY month DESC;


-- ========================================
-- 9. SEARCH QUERIES
-- ========================================

-- Search pets by name
SELECT * FROM pet WHERE name LIKE '%Bruno%';

-- Search pets by breed
SELECT * FROM pet WHERE breed LIKE '%Labrador%';

-- Search users by email
SELECT * FROM users WHERE email LIKE '%example.com%';

-- Search pending submissions by owner name
SELECT * FROM pets_pending WHERE name LIKE '%Satyam%';

-- ========================================
-- 10. RECENT ACTIVITY
-- ========================================

-- Last 10 pets added
SELECT * FROM pet ORDER BY created_at DESC LIMIT 10;

-- Last 10 adoption requests
SELECT * 
FROM adoption_requests 
ORDER BY created_at DESC 
LIMIT 10;


-- Last 10 user signups
SELECT * FROM users ORDER BY created_at DESC LIMIT 10;

-- Last 10 pending submissions
SELECT * FROM pets_pending ORDER BY created_at DESC LIMIT 10;

-- ========================================
-- 11. DATA CLEANUP QUERIES (Use Carefully!)
-- ========================================

-- View OTP entries (for debugging signup issues)
SELECT * FROM otp_verification ORDER BY created_at DESC LIMIT 10;

-- Clean expired OTPs
SET SQL_SAFE_UPDATES = 0;

DELETE FROM otp_verification
WHERE expiry_time < NOW() AND verified = FALSE;

SET SQL_SAFE_UPDATES = 1;


-- View pets with missing images
SELECT * FROM pet WHERE image_url IS NULL OR image_url = '';

-- View pending submissions with missing images
SELECT * FROM pets_pending WHERE image_path IS NULL OR image_path = '';

-- ========================================
-- 12. VERIFICATION QUERIES (For Testing)
-- ========================================

-- Check if specific pet exists
SELECT * FROM pets WHERE pet_id = 1;

-- Check if specific user exists
SELECT * FROM users WHERE email = 'satyampatelkatni2003@gmail.com';


-- Verify admin password hash
SELECT 
    email, 
    password_hash,
    LENGTH(password_hash) as hash_length
FROM admins 
WHERE email = 'satyampatelkatni2003@.com';

-- ========================================
-- 13. QUICK STATUS CHECKS
-- ========================================

-- Are there pending submissions to review?
SELECT COUNT(*) as pending_count 
FROM pets_pending 
WHERE status = 'pending';

-- Are there pending adoption requests?
SELECT COUNT(*) as pending_requests 
FROM adoption_requests 
WHERE status = 'pending';

-- How many available pets?
SELECT COUNT(*) as available_pets 
FROM pet
WHERE status = 'available';

-- ========================================
-- 14. IMAGE PATH QUERIES
-- ========================================

-- Check pet image paths
SELECT id as  pet_id, name, image_url FROM pet;

-- Check pending pet image paths
SELECT id, name, image_path FROM pets_pending;

-- Find pets with full paths (need fixing)
SELECT * FROM pet WHERE image_url LIKE '%\\%' OR image_url LIKE '%/%';

-- Find pending pets with full paths
SELECT * FROM pets_pending WHERE image_path LIKE '%\\%' OR image_path LIKE '%/%';

-- ========================================
-- 15. SPECIFIC USER/PET LOOKUP
-- ========================================

-- Get all info for a specific pet
SELECT * FROM pet WHERE pet_id = 1;

-- Get all adoption requests for a specific pet
SELECT * FROM adoption_requests WHERE pet_id = 1;

-- Get all submissions from a specific user
SELECT * FROM pets_pending WHERE email = 'satyampatelkatni2003@gmail.com';

-- Get specific adoption request with full details
SELECT 
    ar.*,
    p.name AS pet_name,
    p.breed AS pet_breed,
    p.image_url AS pet_image
FROM adoption_requests ar
LEFT JOIN pet p ON ar.pet_id = p.id
WHERE ar.id = 1;


-- doing operations things from my side

select * from users;
-- ========================================
-- END OF MONITORING QUERIES
-- ========================================

/*
QUICK REFERENCE:

Most Used:
- SELECT * FROM pets;
- SELECT * FROM pets_pending WHERE status = 'pending';
- SELECT * FROM adoption_requests WHERE status = 'pending';
- SELECT * FROM users;

For Admin Dashboard:
- Total users: SELECT COUNT(*) FROM users;
- Available pets: SELECT COUNT(*) FROM pets WHERE status = 'available';
- Pending requests: SELECT COUNT(*) FROM adoption_requests WHERE status = 'pending';
- Total adoptions: SELECT COUNT(*) FROM adoption_requests WHERE status = 'approved';
*/

-- ========================================
-- 7. ADMIN CREDENTIALS
-- ========================================
/*
Email: admin@petadoption.com
Password: admin123

BCrypt Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
*/

-- ========================================
-- 8. USAGE EXAMPLES
-- ========================================

-- Example 1: Submit an adoption request using procedure
-- CALL submit_adoption_request(1, 'Bruno', 'Labrador', 3, 'dog', '1718906386213-550689756.jpg', 
--      'user@example.com', '9876543210', 'Apartment', 'Yes, had dogs before', 'Family of 4');

-- Example 2: Approve adoption request using procedure
-- CALL approve_adoption_request(1);

-- Example 3: Reject adoption request using procedure
-- CALL reject_adoption_request(2);

-- Example 4: Get all pending requests using procedure
-- CALL get_pending_requests();

-- ========================================
-- 9. TESTING QUERIES
-- ========================================

-- Test trigger: Insert and update adoption request
-- INSERT INTO adoption_requests (pet_id, pet_name, email, status) VALUES (1, 'Test Pet', 'test@test.com', 'PENDING');
-- UPDATE adoption_requests SET status = 'APPROVED' WHERE pet_id = 1;
-- SELECT status FROM pet WHERE id = 1; -- Should show 'adopted'

-- ==================== CLEANUP SCRIPT (Run if needed) ====================

-- To clean up dummy data from pets_pending:
-- DELETE FROM pets_pending WHERE image_path LIKE '/uploads/%' OR image_path IS NULL;

-- To reset all data:
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE adoption_requests;
-- TRUNCATE TABLE pet;
-- TRUNCATE TABLE pets_pending;
-- TRUNCATE TABLE tracking;
-- SET FOREIGN_KEY_CHECKS = 1;
