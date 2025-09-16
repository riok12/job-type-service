package com.example.job_type_service.service;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import com.example.job_type_service.exception.JobTypeNotFoundException;
import com.example.job_type_service.exception.JobTypeServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PJobTypeServiceSimpleTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PJobTypeService pJobTypeService;

    private JobTypeRequest jobTypeRequest;
    private UpdateJobTypeRequest updateJobTypeRequest;

    @BeforeEach
    void setUp() {
        jobTypeRequest = new JobTypeRequest("FULL_TIME", "Full-time employment", "admin");
        updateJobTypeRequest = new UpdateJobTypeRequest("PART_TIME", "Part-time employment", "admin");
    }

    @Test
    void insertJobType_Success() {
        when(jdbcTemplate.queryForObject("SELECT SEQ_P_JOB_TYPE.NEXTVAL FROM dual", Long.class))
                .thenReturn(1L);
        
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    CallableStatement cs = mock(CallableStatement.class);
                    lenient().when(cs.getString(2)).thenReturn("FULL_TIME");
                    lenient().when(cs.getString(3)).thenReturn("Full-time employment");
                    lenient().when(cs.getTimestamp(4)).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
                    lenient().when(cs.getString(5)).thenReturn("admin");
                    return invocation.getArgument(1, org.springframework.jdbc.core.CallableStatementCallback.class)
                            .doInCallableStatement(cs);
                });

        JobTypeResponse result = pJobTypeService.insertJobType(jobTypeRequest);
        assertNotNull(result);
        assertEquals(1L, result.getPJobTypeId());
        assertEquals("FULL_TIME", result.getCode());
        assertEquals("Full-time employment", result.getDescription());
        assertEquals("admin", result.getUpdateBy());

        System.out.println("=== INSERT JOB TYPE SUCCESS ===");
        System.out.println("Input: " + jobTypeRequest);
        System.out.println("Result: " + result);
        System.out.println("Generated ID: " + result.getPJobTypeId());
        System.out.println("===============================");
        
        verify(jdbcTemplate).queryForObject("SELECT SEQ_P_JOB_TYPE.NEXTVAL FROM dual", Long.class);
        verify(jdbcTemplate, times(2)).execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class));
    }

    @Test
    void insertJobType_SQLException() {
        when(jdbcTemplate.queryForObject("SELECT SEQ_P_JOB_TYPE.NEXTVAL FROM dual", Long.class))
                .thenReturn(1L);
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenThrow(new RuntimeException("SQL Error", new SQLException("Database error")));

        JobTypeServiceException exception = assertThrows(JobTypeServiceException.class, 
                () -> pJobTypeService.insertJobType(jobTypeRequest));
        assertTrue(exception.getMessage().contains("Failed to insert job type"));
        System.out.println("=== INSERT JOB TYPE SQL EXCEPTION ===");
        System.out.println("Input: " + jobTypeRequest);
        System.out.println("Exception: " + exception.getClass().getSimpleName());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("=====================================");
    }

    @Test
    void updateJobType_Success() {
        Long id = 1L;
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    CallableStatement cs = mock(CallableStatement.class);
                    lenient().when(cs.getString(2)).thenReturn("PART_TIME");
                    lenient().when(cs.getString(3)).thenReturn("Part-time employment");
                    lenient().when(cs.getTimestamp(4)).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
                    lenient().when(cs.getString(5)).thenReturn("admin");
                    return invocation.getArgument(1, org.springframework.jdbc.core.CallableStatementCallback.class)
                            .doInCallableStatement(cs);
                });

        JobTypeResponse result = pJobTypeService.updateJobType(id, updateJobTypeRequest);
        assertNotNull(result);
        assertEquals(id, result.getPJobTypeId());
        assertEquals("PART_TIME", result.getCode());
        assertEquals("Part-time employment", result.getDescription());
        assertEquals("admin", result.getUpdateBy());

        System.out.println("=== UPDATE JOB TYPE SUCCESS ===");
        System.out.println("ID: " + id);
        System.out.println("Input: " + updateJobTypeRequest);
        System.out.println("Result: " + result);
        System.out.println("===============================");
        
        verify(jdbcTemplate, times(2)).execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class));
    }

    @Test
    void updateJobType_JobTypeNotFound() {
        Long id = 999L;
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenThrow(new RuntimeException("Job type with ID 999 not found", 
                        new SQLException("Not found", "SQL", 20001)));
        JobTypeNotFoundException exception = assertThrows(JobTypeNotFoundException.class, 
                () -> pJobTypeService.updateJobType(id, updateJobTypeRequest));
        assertEquals("Job type with ID 999 not found", exception.getMessage());

        System.out.println("=== UPDATE JOB TYPE NOT FOUND ===");
        System.out.println("ID: " + id);
        System.out.println("Input: " + updateJobTypeRequest);
        System.out.println("Exception: " + exception.getClass().getSimpleName());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("==================================");
    }

    @Test
    void getJobTypeById_Success() {
        Long id = 1L;
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    CallableStatement cs = mock(CallableStatement.class);
                    when(cs.getString(2)).thenReturn("FULL_TIME");
                    when(cs.getString(3)).thenReturn("Full-time employment");
                    when(cs.getTimestamp(4)).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
                    when(cs.getString(5)).thenReturn("admin");
                    return invocation.getArgument(1, org.springframework.jdbc.core.CallableStatementCallback.class)
                            .doInCallableStatement(cs);
                });

        JobTypeResponse result = pJobTypeService.getJobTypeById(id);
        assertNotNull(result);
        assertEquals(id, result.getPJobTypeId());
        assertEquals("FULL_TIME", result.getCode());
        assertEquals("Full-time employment", result.getDescription());
        assertEquals("admin", result.getUpdateBy());

        System.out.println("=== GET JOB TYPE BY ID SUCCESS ===");
        System.out.println("ID: " + id);
        System.out.println("Result: " + result);
        System.out.println("===================================");
        
        verify(jdbcTemplate).execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class));
    }

    @Test
    void getJobTypeById_NotFound() {
        Long id = 999L;
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    CallableStatement cs = mock(CallableStatement.class);
                    when(cs.getString(2)).thenReturn(null);
                    return invocation.getArgument(1, org.springframework.jdbc.core.CallableStatementCallback.class)
                            .doInCallableStatement(cs);
                });

        JobTypeNotFoundException exception = assertThrows(JobTypeNotFoundException.class, 
                () -> pJobTypeService.getJobTypeById(id));
        assertEquals("Job type with ID 999 not found", exception.getMessage());

        System.out.println("=== GET JOB TYPE BY ID NOT FOUND ===");
        System.out.println("ID: " + id);
        System.out.println("Exception: " + exception.getClass().getSimpleName());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("=====================================");
    }

    @Test
    void deleteJobType_Success() {
        Long id = 1L;
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenAnswer(invocation -> 
                        invocation.getArgument(1, org.springframework.jdbc.core.CallableStatementCallback.class)
                                .doInCallableStatement(mock(CallableStatement.class)));

        assertDoesNotThrow(() -> pJobTypeService.deleteJobType(id));

        System.out.println("=== DELETE JOB TYPE SUCCESS ===");
        System.out.println("ID: " + id);
        System.out.println("Status: Successfully deleted");
        System.out.println("===============================");
        
        verify(jdbcTemplate).execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class));
    }

    @Test
    void deleteJobType_NotFound() {
        Long id = 999L;
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenThrow(new RuntimeException("Job type with ID 999 not found", 
                        new SQLException("Not found", "SQL", 20002)));

        JobTypeNotFoundException exception = assertThrows(JobTypeNotFoundException.class, 
                () -> pJobTypeService.deleteJobType(id));
        assertEquals("Job type with ID 999 not found", exception.getMessage());
    }

    @Test
    void getAllJobTypes_Success() throws SQLException {
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    CallableStatement cs = mock(CallableStatement.class);
                    ResultSet rs = mock(ResultSet.class);
                    when(cs.getObject(1)).thenReturn(rs);
                    when(rs.next()).thenReturn(true).thenReturn(false);
                    when(rs.getLong("P_JOB_TYPE_ID")).thenReturn(1L);
                    when(rs.getString("CODE")).thenReturn("FULL_TIME");
                    when(rs.getString("DESCRIPTION")).thenReturn("Full-time employment");
                    when(rs.getTimestamp("UPDATE_DATE")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
                    when(rs.getString("UPDATE_BY")).thenReturn("admin");
                    return invocation.getArgument(1, org.springframework.jdbc.core.CallableStatementCallback.class)
                            .doInCallableStatement(cs);
                });

        List<JobTypeResponse> result = pJobTypeService.getAllJobTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getPJobTypeId());
        assertEquals("FULL_TIME", result.get(0).getCode());
        
        verify(jdbcTemplate).execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class));
    }

    @Test
    void getAllJobTypes_SQLException() {
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenThrow(new RuntimeException("SQL Error", new SQLException("Database error")));

        JobTypeServiceException exception = assertThrows(JobTypeServiceException.class, 
                () -> pJobTypeService.getAllJobTypes());
        assertTrue(exception.getMessage().contains("Failed to retrieve all job types"));
    }

    @Test
    void getJobTypeByCode_Success() {
        String code = "FULL_TIME";
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    CallableStatement cs = mock(CallableStatement.class);
                    when(cs.getLong(2)).thenReturn(1L);
                    when(cs.getString(3)).thenReturn("Full-time employment");
                    when(cs.getTimestamp(4)).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
                    when(cs.getString(5)).thenReturn("admin");
                    return invocation.getArgument(1, org.springframework.jdbc.core.CallableStatementCallback.class)
                            .doInCallableStatement(cs);
                });
        JobTypeResponse result = pJobTypeService.getJobTypeByCode(code);

        assertNotNull(result);
        assertEquals(1L, result.getPJobTypeId());
        assertEquals(code, result.getCode());
        assertEquals("Full-time employment", result.getDescription());
        assertEquals("admin", result.getUpdateBy());
        
        verify(jdbcTemplate).execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class));
    }

    @Test
    void getJobTypeByCode_NotFound() {
        String code = "INVALID_CODE";
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    CallableStatement cs = mock(CallableStatement.class);
                    when(cs.getLong(2)).thenReturn(0L);
                    return invocation.getArgument(1, org.springframework.jdbc.core.CallableStatementCallback.class)
                            .doInCallableStatement(cs);
                });

        JobTypeNotFoundException exception = assertThrows(JobTypeNotFoundException.class, 
                () -> pJobTypeService.getJobTypeByCode(code));
        assertEquals("Job type with code INVALID_CODE not found", exception.getMessage());
    }

    @Test
    void getJobTypeByCode_SQLException() {
        String code = "FULL_TIME";
        when(jdbcTemplate.execute(anyString(), any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenThrow(new RuntimeException("SQL Error", new SQLException("Database error")));

        JobTypeServiceException exception = assertThrows(JobTypeServiceException.class, 
                () -> pJobTypeService.getJobTypeByCode(code));
        assertTrue(exception.getMessage().contains("Failed to retrieve job type by code"));
    }
}
