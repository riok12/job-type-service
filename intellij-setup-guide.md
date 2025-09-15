# IntelliJ IDEA Oracle Database Setup Guide

## Konfigurasi Data Source

### 1. Data Source Configuration
```
Name: Oracle XE Local
Host: localhost
Port: 1521
SID: XE
User: system
Password: oracle
URL: jdbc:oracle:thin:@localhost:1521:XE
```

### 2. Driver Configuration
- **Driver Class**: `oracle.jdbc.OracleDriver`
- **Driver Files**: `/Users/zulfan/.m2/repository/com/oracle/database/jdbc/ojdbc11/23.2.0.0/ojdbc11-23.2.0.0.jar`

### 3. Test Connection
Setelah konfigurasi selesai, klik "Test Connection" - harus muncul "Connection successful!"

## SQL Queries untuk Testing

### Test Basic Connection
```sql
SELECT 'Connection successful!' as status FROM dual;
```

### Check Our Tables
```sql
SELECT table_name FROM user_tables WHERE table_name = 'P_JOB_TYPE';
```

### Check Our Sequences
```sql
SELECT sequence_name FROM user_sequences WHERE sequence_name = 'SEQ_P_JOB_TYPE';
```

### Check Our Packages
```sql
SELECT object_name FROM user_objects WHERE object_type = 'PACKAGE' AND object_name = 'PACK_TEST';
```

### View Job Types Data
```sql
SELECT * FROM P_JOB_TYPE ORDER BY P_JOB_TYPE_ID;
```

### Insert Test Data
```sql
INSERT INTO P_JOB_TYPE (P_JOB_TYPE_ID, CODE, DESCRIPTION, UPDATE_DATE, UPDATE_BY)
VALUES (SEQ_P_JOB_TYPE.NEXTVAL, 'TEST001', 'Test Job Type', SYSTIMESTAMP, 'intellij_user');
```

### Update Test Data
```sql
UPDATE P_JOB_TYPE 
SET DESCRIPTION = 'Updated Test Job Type', UPDATE_DATE = SYSTIMESTAMP, UPDATE_BY = 'intellij_user'
WHERE CODE = 'TEST001';
```

### Delete Test Data
```sql
DELETE FROM P_JOB_TYPE WHERE CODE = 'TEST001';
```

## Troubleshooting

### Jika "No driver files configured" muncul:
1. Klik tab "Drivers" di sebelah kiri
2. Pilih "Oracle"
3. Klik "+" untuk menambah driver
4. Set Driver Class: `oracle.jdbc.OracleDriver`
5. Tambahkan JAR file: `/Users/zulfan/.m2/repository/com/oracle/database/jdbc/ojdbc11/23.2.0.0/ojdbc11-23.2.0.0.jar`

### Jika connection failed:
1. Pastikan Oracle XE container running: `docker ps`
2. Pastikan port 1521 accessible: `lsof -i :1521`
3. Test dari aplikasi: `curl http://localhost:8081/api/database/info`
