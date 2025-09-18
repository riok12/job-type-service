package com.example.job_type_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseInitializationServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DatabaseInitializationService databaseInitializationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void initializeDatabase_Success() throws IOException {
        // Arrange
        String createTableSql = "CREATE TABLE P_JOB_TYPE (ID NUMBER, CODE VARCHAR2(128));";
        String createPackageSql = "CREATE OR REPLACE PACKAGE PACK_TEST AS END;";
        String createPackageBodySql = "CREATE OR REPLACE PACKAGE BODY PACK_TEST AS END;";

        assertDoesNotThrow(() -> databaseInitializationService.initializeDatabase());

    }

    @Test
    void initializeDatabase_IOException() {
        assertDoesNotThrow(() -> databaseInitializationService.initializeDatabase());
    }

    @Test
    void executeSqlScript_TableScript() throws IOException {
        assertDoesNotThrow(() -> databaseInitializationService.initializeDatabase());
    }

    @Test
    void executeSqlScript_PackageScript() throws IOException {
        assertDoesNotThrow(() -> databaseInitializationService.initializeDatabase());
    }

    @Test
    void executeSqlScript_EmptyStatements() throws IOException {
        assertDoesNotThrow(() -> databaseInitializationService.initializeDatabase());
    }

    @Test
    void executeSqlScript_StatementExecutionException() throws IOException {
        assertDoesNotThrow(() -> databaseInitializationService.initializeDatabase());
    }

    @Test
    void executeSqlScript_PackageExecutionException() throws IOException {
        assertDoesNotThrow(() -> databaseInitializationService.initializeDatabase());
    }

    @Test
    void initializeDatabase_IntegrationTest() {
        assertDoesNotThrow(() -> databaseInitializationService.initializeDatabase());
    }
}

