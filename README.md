# Job Type Service

Spring Boot application yang menyediakan REST API untuk mengelola data job type dengan integrasi Oracle Database menggunakan stored procedures.

## Setup Oracle Database XE

### 1. Install Oracle Database XE

#### Untuk macOS:
```bash
# Download Oracle Database XE dari Oracle website
# Atau menggunakan Homebrew (jika tersedia)
brew install oracle-instantclient
```

#### Untuk Windows:
1. Download Oracle Database XE dari [Oracle Downloads](https://www.oracle.com/database/technologies/xe-downloads.html)
2. Jalankan installer dan ikuti petunjuk instalasi
3. Set password untuk user SYSTEM

#### Untuk Linux:
```bash
# Download Oracle Database XE dari Oracle website
# Install sesuai dengan petunjuk Oracle
```

### 2. Konfigurasi Database

Setelah instalasi selesai, buat koneksi ke database:

```sql
-- Login sebagai SYSTEM
sqlplus system/password@localhost:1521/xe

-- Buat user baru (opsional)
CREATE USER jobtype_user IDENTIFIED BY password;
GRANT CONNECT, RESOURCE TO jobtype_user;
GRANT CREATE TABLE, CREATE SEQUENCE, CREATE PROCEDURE TO jobtype_user;
```

### 3. Setup Database Schema

Jalankan script SQL berikut untuk membuat tabel dan stored procedures:

```bash
# Login ke database
sqlplus system/password@localhost:1521/xe

# Jalankan script untuk membuat tabel
@src/main/resources/sql/create_table.sql

# Jalankan script untuk membuat package
@src/main/resources/sql/create_package.sql

# Jalankan script untuk membuat package body
@src/main/resources/sql/create_package_body.sql
```

## Konfigurasi Application

### 1. Update application.properties

Pastikan konfigurasi database sesuai dengan setup Oracle Anda:

```properties
# Oracle Database Configuration
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=system
spring.datasource.password=your_password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

### 2. Dependencies

Project sudah menggunakan:
- Spring Boot 3.5.5
- Oracle JDBC Driver (ojdbc11)
- Lombok untuk mengurangi boilerplate code
- Spring Boot Starter Web untuk REST API
- Spring Boot Starter Data JPA untuk database operations

## Menjalankan Application

### 1. Build Project
```bash
./mvnw clean compile
```

### 2. Run Application
```bash
./mvnw spring-boot:run
```

Application akan berjalan di port 8081.

## REST API Endpoints

### 1. Create Job Type
```http
POST /api/job-types
Content-Type: application/json

{
    "code": "FULL_TIME",
    "description": "Full Time Employee",
    "updateBy": "admin"
}
```

### 2. Update Job Type
```http
PUT /api/job-types/{id}
Content-Type: application/json

{
    "code": "FULL_TIME_UPDATED",
    "description": "Updated Full Time Employee",
    "updateBy": "admin"
}
```

### 3. Get Job Type by ID
```http
GET /api/job-types/{id}
```

### 4. Get All Job Types
```http
GET /api/job-types
```

### 5. Get Job Type by Code
```http
GET /api/job-types/code/{code}
```

### 6. Delete Job Type
```http
DELETE /api/job-types/{id}
```

## Database Schema

### Tabel P_JOB_TYPE
- `P_JOB_TYPE_ID` (NUMBER) - Primary Key
- `CODE` (VARCHAR2(128)) - Unique job type code
- `DESCRIPTION` (VARCHAR2(512)) - Job type description
- `UPDATE_DATE` (TIMESTAMP) - Last update timestamp
- `UPDATE_BY` (VARCHAR2(128)) - User who updated the record

### Stored Procedures (PACK_TEST Package)
- `insert_job_type` - Insert new job type
- `update_job_type` - Update existing job type
- `view_job_type` - Get job type by ID
- `delete_job_type` - Delete job type by ID

## Testing

### Test Database Connection
```bash
# Test koneksi database
sqlplus system/password@localhost:1521/xe
```

### Test API dengan curl
```bash
# Create job type
curl -X POST http://localhost:8081/api/job-types \
  -H "Content-Type: application/json" \
  -d '{"code":"TEST","description":"Test Job Type","updateBy":"admin"}'

# Get job type
curl http://localhost:8081/api/job-types/1
```

## Troubleshooting

### 1. Database Connection Issues
- Pastikan Oracle Database XE sudah running
- Cek username/password di application.properties
- Pastikan port 1521 tidak terblokir

### 2. Stored Procedure Issues
- Pastikan semua script SQL sudah dijalankan
- Cek apakah package PACK_TEST sudah dibuat dengan benar
- Pastikan sequence SEQ_P_JOB_TYPE sudah dibuat

### 3. Application Issues
- Pastikan Java 21 sudah terinstall
- Cek log application untuk error details
- Pastikan semua dependencies sudah terdownload

## Development Notes

- Service menggunakan JdbcTemplate untuk memanggil stored procedures
- Entity menggunakan Lombok untuk mengurangi boilerplate code
- DTO classes menggunakan validation annotations
- Controller menggunakan standard HTTP status codes
- CORS sudah dikonfigurasi untuk development
