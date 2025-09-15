# Oracle Database XE Setup Guide

## Quick Setup untuk Development

### 1. Install Oracle Database XE

#### macOS:
```bash
# Download dari Oracle website atau gunakan Docker
docker run -d --name oracle-xe \
  -p 1521:1521 \
  -e ORACLE_PWD=oracle \
  container-registry.oracle.com/database/express:21.3.0-xe
```

#### Windows:
1. Download Oracle Database XE dari [Oracle Downloads](https://www.oracle.com/database/technologies/xe-downloads.html)
2. Install dengan default settings
3. Password untuk SYSTEM: oracle (atau sesuai yang Anda set)

#### Linux:
```bash
# Download dan install sesuai petunjuk Oracle
# Atau gunakan Docker seperti di macOS
```

### 2. Setup Database Schema

```bash
# Login ke database
sqlplus system/oracle@localhost:1521/xe

# Jalankan setup script
@setup-database.sql
```

### 3. Test Connection

```bash
# Test koneksi dari command line
sqlplus system/oracle@localhost:1521/xe

# Atau test dengan aplikasi
./mvnw spring-boot:run
```

### 4. Test API

```bash
# Jalankan test script
./test-api.sh
```

## Troubleshooting

### Connection Issues:
- Pastikan Oracle Database sudah running
- Cek port 1521 tidak terblokir
- Pastikan username/password benar

### Database Issues:
- Pastikan semua script SQL sudah dijalankan
- Cek apakah tabel dan stored procedures sudah dibuat
- Pastikan sequence sudah dibuat

### Application Issues:
- Pastikan Java 21 sudah terinstall
- Cek log aplikasi untuk error details
- Pastikan dependencies sudah terdownload

## Default Configuration

- **Host**: localhost
- **Port**: 1521
- **SID**: xe
- **Username**: system
- **Password**: oracle
- **Application Port**: 8081
