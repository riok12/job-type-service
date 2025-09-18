# Job Type Service - JPA Version 2

This is version 2 of the Job Type Service using JPA Repository pattern instead of JDBC with stored procedures.

## Overview

The JPA version provides the same functionality as the original service but uses Spring Data JPA for database operations, offering better abstraction, type safety, and easier maintenance.

## Key Differences from Version 1

### Architecture Changes
- **Repository Pattern**: Uses `PJobTypeRepository` extending `JpaRepository`
- **Entity Management**: Leverages JPA entity lifecycle management
- **Transaction Management**: Uses `@Transactional` annotations
- **Query Methods**: Utilizes Spring Data JPA query methods and custom queries
- **Hybrid Approach**: Combines JPA Repository with manual Oracle sequence generation for ID assignment

### API Endpoints

The JPA version is available at `/api/v2/job-types` endpoints:

#### Basic CRUD Operations
- `POST /api/v2/job-types` - Create a new job type
- `GET /api/v2/job-types/{id}` - Get job type by ID
- `GET /api/v2/job-types/code/{code}` - Get job type by code
- `PUT /api/v2/job-types/{id}` - Update job type by ID
- `DELETE /api/v2/job-types/{id}` - Delete job type by ID
- `DELETE /api/v2/job-types/code/{code}` - Delete job type by code

#### Advanced Operations
- `GET /api/v2/job-types` - Get all job types
- `GET /api/v2/job-types/ordered/update-date-desc` - Get all job types ordered by update date (descending)
- `GET /api/v2/job-types/ordered/code-asc` - Get all job types ordered by code (ascending)
- `GET /api/v2/job-types/search/description?description=text` - Search job types by description
- `GET /api/v2/job-types/filter/update-by?updateBy=user` - Filter by update user
- `GET /api/v2/job-types/filter/updated-after?updateDate=2024-01-01T00:00:00` - Filter by update date (after)
- `GET /api/v2/job-types/filter/updated-between?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59` - Filter by date range

#### Utility Operations
- `GET /api/v2/job-types/exists/{id}` - Check if job type exists by ID
- `GET /api/v2/job-types/exists/code/{code}` - Check if job type exists by code
- `GET /api/v2/job-types/count` - Get total count of job types
- `GET /api/v2/job-types/count/update-by?updateBy=user` - Count job types by update user

## Features

### Enhanced Entity Management
- **Manual ID Generation**: Uses Oracle sequence `SEQ_P_JOB_TYPE` with manual sequence call
- **Validation**: Built-in validation with Jakarta validation annotations
- **Unique Constraints**: Code field has unique constraint
- **Audit Fields**: Automatic update date management

### Repository Features
- **Query Methods**: Spring Data JPA derived query methods
- **Custom Queries**: JPQL queries for complex operations
- **Pagination Support**: Ready for pagination implementation
- **Batch Operations**: Optimized for batch processing

### Service Layer Benefits
- **Transaction Management**: Automatic transaction handling
- **Exception Handling**: Consistent error handling
- **Type Safety**: Compile-time type checking
- **Performance**: Optimized queries and caching support
- **Hybrid Approach**: Combines JPA Repository with manual sequence generation for Oracle compatibility

## Database Schema

The JPA version uses the same database schema as version 1:

```sql
CREATE TABLE P_JOB_TYPE (
    P_JOB_TYPE_ID NUMBER PRIMARY KEY,
    CODE VARCHAR2(128) NOT NULL UNIQUE,
    DESCRIPTION VARCHAR2(512),
    UPDATE_DATE TIMESTAMP NOT NULL,
    UPDATE_BY VARCHAR2(128) NOT NULL
);

CREATE SEQUENCE SEQ_P_JOB_TYPE START WITH 1 INCREMENT BY 1;
```

## Configuration

### Application Properties
```properties
# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
spring.jpa.properties.hibernate.connection.provider_disables_autocommit=true
spring.jpa.open-in-view=false
```

## Usage Examples

### Creating a Job Type
```bash
curl -X POST http://localhost:8081/api/v2/job-types \
  -H "Content-Type: application/json" \
  -d '{
    "code": "DEVELOPER",
    "description": "Software Developer",
    "updateBy": "admin"
  }'
```

### Getting All Job Types Ordered by Update Date
```bash
curl http://localhost:8081/api/v2/job-types/ordered/update-date-desc
```

### Searching by Description
```bash
curl "http://localhost:8081/api/v2/job-types/search/description?description=developer"
```

### Filtering by Update User
```bash
curl "http://localhost:8081/api/v2/job-types/filter/update-by?updateBy=admin"
```

## Testing

The JPA version includes comprehensive unit tests:
- `PJobTypeJpaServiceTest` - Service layer tests with Mockito
- Repository tests can be added for integration testing

## Performance Considerations

### Optimizations
- **Batch Processing**: Configured for batch operations
- **Connection Pooling**: Uses HikariCP by default
- **Query Optimization**: Optimized Hibernate settings
- **Lazy Loading**: Disabled open-in-view for better performance

### Monitoring
- SQL logging enabled for development
- Formatted SQL output for readability
- Transaction boundary logging

## Migration from Version 1

To migrate from the JDBC version to JPA version:

1. **Update API calls**: Change endpoints from `/api/job-types` to `/api/v2/job-types`
2. **Response format**: Same DTOs are used, so no changes needed
3. **Error handling**: Same exception types are thrown
4. **Database**: Same schema, no migration needed

## Benefits of JPA Version

1. **Maintainability**: Cleaner, more readable code
2. **Type Safety**: Compile-time checking
3. **Performance**: Optimized queries and caching
4. **Flexibility**: Easy to add new query methods
5. **Testing**: Better testability with repository mocking
6. **Standards**: Follows JPA standards and best practices

## Future Enhancements

- **Pagination**: Add pagination support for large datasets
- **Caching**: Implement Redis caching for frequently accessed data
- **Audit**: Add audit trail functionality
- **Soft Delete**: Implement soft delete instead of hard delete
- **Bulk Operations**: Add bulk insert/update operations
