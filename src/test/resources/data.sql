-- From Chat Gpt

-- Clean up first (important for repeatable testing)
DELETE FROM application_notes;
DELETE FROM job_applications;
DELETE FROM job_entries;
DELETE FROM users;

-- Reset PostgreSQL sequences
ALTER SEQUENCE users_user_id_seq RESTART WITH 1;
ALTER SEQUENCE job_entries_job_id_seq RESTART WITH 1;
ALTER SEQUENCE job_applications_application_id_seq RESTART WITH 1;
ALTER SEQUENCE application_notes_notes_id_seq RESTART WITH 1;

-- =====================================================
-- 1. USERS
-- 3 users total:
--   1 admin
--   2 normal users
-- =====================================================
INSERT INTO users (username, role, oauth_provider, email) VALUES
('admin1', 'ADMIN', 'google', 'admin1@example.com'),
('user1',  'USER',  'google', 'user1@example.com'),
('user2',  'USER',  'github', 'user2@example.com');

-- =====================================================
-- 2. JOB ENTRIES
-- each normal user gets 5 jobs
-- admin gets 0 jobs
-- user1 => user_id = 2
-- user2 => user_id = 3
-- =====================================================
INSERT INTO job_entries (user_id, company, job_title, salary_text) VALUES
-- user1 jobs
(2, 'Google',    'Software Engineer Intern', '$45/hr'),
(2, 'Amazon',    'Data Analyst Intern',      '$42/hr'),
(2, 'Meta',      'Backend Developer Intern', '$48/hr'),
(2, 'Apple',     'Machine Learning Intern',  '$50/hr'),
(2, 'Netflix',   'Data Engineer Intern',     '$46/hr'),

-- user2 jobs
(3, 'Tesla',     'Software Engineer Intern', '$44/hr'),
(3, 'NVIDIA',    'Data Scientist Intern',    '$47/hr'),
(3, 'Adobe',     'Frontend Developer Intern','$41/hr'),
(3, 'Uber',      'Backend Engineer Intern',  '$43/hr'),
(3, 'LinkedIn',  'Product Analyst Intern',   '$40/hr');

-- =====================================================
-- 3. JOB APPLICATIONS
-- each job entry has one application
-- user1 jobs => job_id 1~5
-- user2 jobs => job_id 6~10
-- =====================================================
INSERT INTO job_applications (user_id, job_id, status, date_applied) VALUES
-- user1 applications
(2, 1, 'APPLIED',     '2026-03-01'),
(2, 2, 'INTERVIEW',   '2026-03-02'),
(2, 3, 'APPLIED',     '2026-03-03'),
(2, 4, 'REJECTED',    '2026-03-04'),
(2, 5, 'OFFER',       '2026-03-05'),

-- user2 applications
(3, 6, 'APPLIED',     '2026-03-01'),
(3, 7, 'INTERVIEW',   '2026-03-02'),
(3, 8, 'APPLIED',     '2026-03-03'),
(3, 9, 'REJECTED',    '2026-03-04'),
(3, 10, 'APPLIED',    '2026-03-05');

-- =====================================================
-- 4. APPLICATION NOTES
-- two notes per normal user
-- total = 4 notes
-- user1 => application_id 1, 3
-- user2 => application_id 6, 8
-- =====================================================
INSERT INTO application_notes (application_id, content) VALUES
(1, 'Need to follow up with recruiter next week.'),
(3, 'Referred by a classmate from school.'),
(6, 'Customized resume submitted for this role.'),
(8, 'Need to prepare portfolio before interview.');