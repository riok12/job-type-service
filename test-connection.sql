-- Test Oracle Database Connection
-- Run this in IntelliJ IDEA SQL Console

-- Test basic connection
SELECT 'Connection successful!' as status FROM dual;

-- Check if our table exists
SELECT table_name FROM user_tables WHERE table_name = 'P_JOB_TYPE';

-- Check if our sequence exists
SELECT sequence_name FROM user_sequences WHERE sequence_name = 'SEQ_P_JOB_TYPE';

-- Check if our package exists
SELECT object_name FROM user_objects WHERE object_type = 'PACKAGE' AND object_name = 'PACK_TEST';

-- Test data insertion
SELECT COUNT(*) as total_job_types FROM P_JOB_TYPE;
