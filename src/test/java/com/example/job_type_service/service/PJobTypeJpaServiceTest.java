package com.example.job_type_service.service;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import com.example.job_type_service.entity.PJobType;
import com.example.job_type_service.exception.JobTypeNotFoundException;
import com.example.job_type_service.exception.JobTypeServiceException;
import com.example.job_type_service.repository.PJobTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PJobTypeJpaServiceTest {

    @Mock
    private PJobTypeRepository jobTypeRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PJobTypeJpaService jobTypeJpaService;

    private PJobType testJobType;
    private JobTypeRequest testJobTypeRequest;
    private UpdateJobTypeRequest testUpdateRequest;

    @BeforeEach
    void setUp() {
        testJobType = new PJobType();
        testJobType.setPJobTypeId(1L);
        testJobType.setCode("FULL_TIME");
        testJobType.setDescription("Full-time employment");
        testJobType.setUpdateDate(LocalDateTime.now());
        testJobType.setUpdateBy("admin");

        testJobTypeRequest = new JobTypeRequest("FULL_TIME", "Full-time employment", "admin");

        testUpdateRequest = new UpdateJobTypeRequest("PART_TIME", "Part-time employment", "admin");
    }

    @Test
    void insertJobType_Success() {
        // Given
        Long newId = 1L;
        when(jobTypeRepository.existsByCode(testJobTypeRequest.getCode())).thenReturn(false);
        when(jdbcTemplate.queryForObject("SELECT SEQ_P_JOB_TYPE.NEXTVAL FROM dual", Long.class)).thenReturn(newId);
        when(jobTypeRepository.save(any(PJobType.class))).thenReturn(testJobType);

        // When
        JobTypeResponse result = jobTypeJpaService.insertJobType(testJobTypeRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getPJobTypeId());
        assertEquals("FULL_TIME", result.getCode());
        assertEquals("Full-time employment", result.getDescription());
        assertEquals("admin", result.getUpdateBy());

        System.out.println("=== INSERT JOB TYPE SUCCESS (JPA) ===");
        System.out.println("Input: " + testJobTypeRequest);
        System.out.println("Result: " + result);
        System.out.println("Generated ID: " + result.getPJobTypeId());
        System.out.println("=====================================");
        
        verify(jobTypeRepository).existsByCode(testJobTypeRequest.getCode());
        verify(jdbcTemplate).queryForObject("SELECT SEQ_P_JOB_TYPE.NEXTVAL FROM dual", Long.class);
        verify(jobTypeRepository).save(any(PJobType.class));
    }

    @Test
    void insertJobType_CodeAlreadyExists_ThrowsException() {
        // Given
        when(jobTypeRepository.existsByCode(testJobTypeRequest.getCode())).thenReturn(true);

        // When & Then
        JobTypeServiceException exception = assertThrows(JobTypeServiceException.class,
                () -> jobTypeJpaService.insertJobType(testJobTypeRequest));

        assertEquals("Job type with code 'FULL_TIME' already exists", exception.getMessage());
        
        System.out.println("=== INSERT JOB TYPE CODE ALREADY EXISTS (JPA) ===");
        System.out.println("Input: " + testJobTypeRequest);
        System.out.println("Exception: " + exception.getClass().getSimpleName());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("==================================================");
        
        verify(jobTypeRepository).existsByCode(testJobTypeRequest.getCode());
        verify(jobTypeRepository, never()).save(any(PJobType.class));
    }

    @Test
    void updateJobType_Success() {
        // Given
        Long id = 1L;
        when(jobTypeRepository.findById(id)).thenReturn(Optional.of(testJobType));
        when(jobTypeRepository.existsByCode(testUpdateRequest.getCode())).thenReturn(false);
        when(jobTypeRepository.save(any(PJobType.class))).thenReturn(testJobType);

        // When
        JobTypeResponse result = jobTypeJpaService.updateJobType(id, testUpdateRequest);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getPJobTypeId());
        assertEquals("PART_TIME", result.getCode());
        assertEquals("Part-time employment", result.getDescription());
        assertEquals("admin", result.getUpdateBy());

        System.out.println("=== UPDATE JOB TYPE SUCCESS (JPA) ===");
        System.out.println("ID: " + id);
        System.out.println("Input: " + testUpdateRequest);
        System.out.println("Result: " + result);
        System.out.println("=====================================");
        
        verify(jobTypeRepository).findById(id);
        verify(jobTypeRepository).existsByCode(testUpdateRequest.getCode());
        verify(jobTypeRepository).save(any(PJobType.class));
    }

    @Test
    void updateJobType_NotFound_ThrowsException() {
        // Given
        Long id = 999L;
        when(jobTypeRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        JobTypeNotFoundException exception = assertThrows(JobTypeNotFoundException.class,
                () -> jobTypeJpaService.updateJobType(id, testUpdateRequest));

        assertEquals("Job type with ID 999 not found", exception.getMessage());
        
        System.out.println("=== UPDATE JOB TYPE NOT FOUND (JPA) ===");
        System.out.println("ID: " + id);
        System.out.println("Input: " + testUpdateRequest);
        System.out.println("Exception: " + exception.getClass().getSimpleName());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("========================================");
        
        verify(jobTypeRepository).findById(id);
        verify(jobTypeRepository, never()).save(any(PJobType.class));
    }

    @Test
    void getJobTypeById_Success() {
        // Given
        Long id = 1L;
        when(jobTypeRepository.findById(id)).thenReturn(Optional.of(testJobType));

        // When
        JobTypeResponse result = jobTypeJpaService.getJobTypeById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getPJobTypeId());
        assertEquals("FULL_TIME", result.getCode());
        assertEquals("Full-time employment", result.getDescription());
        assertEquals("admin", result.getUpdateBy());

        System.out.println("=== GET JOB TYPE BY ID SUCCESS (JPA) ===");
        System.out.println("ID: " + id);
        System.out.println("Result: " + result);
        System.out.println("=======================================");
        
        verify(jobTypeRepository).findById(id);
    }

    @Test
    void getJobTypeById_NotFound_ThrowsException() {
        // Given
        Long id = 999L;
        when(jobTypeRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        JobTypeNotFoundException exception = assertThrows(JobTypeNotFoundException.class,
                () -> jobTypeJpaService.getJobTypeById(id));

        assertEquals("Job type with ID 999 not found", exception.getMessage());
        
        System.out.println("=== GET JOB TYPE BY ID NOT FOUND (JPA) ===");
        System.out.println("ID: " + id);
        System.out.println("Exception: " + exception.getClass().getSimpleName());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("==========================================");
        
        verify(jobTypeRepository).findById(id);
    }

    @Test
    void getJobTypeByCode_Success() {
        // Given
        String code = "FULL_TIME";
        when(jobTypeRepository.findByCode(code)).thenReturn(Optional.of(testJobType));

        // When
        JobTypeResponse result = jobTypeJpaService.getJobTypeByCode(code);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getPJobTypeId());
        assertEquals(code, result.getCode());
        assertEquals("Full-time employment", result.getDescription());
        assertEquals("admin", result.getUpdateBy());
        
        System.out.println("=== GET JOB TYPE BY CODE SUCCESS (JPA) ===");
        System.out.println("Code: " + code);
        System.out.println("Result: " + result);
        System.out.println("=========================================");
        
        verify(jobTypeRepository).findByCode(code);
    }

    @Test
    void getJobTypeByCode_NotFound_ThrowsException() {
        // Given
        String code = "INVALID_CODE";
        when(jobTypeRepository.findByCode(code)).thenReturn(Optional.empty());

        // When & Then
        JobTypeNotFoundException exception = assertThrows(JobTypeNotFoundException.class,
                () -> jobTypeJpaService.getJobTypeByCode(code));

        assertEquals("Job type with code 'INVALID_CODE' not found", exception.getMessage());
        
        System.out.println("=== GET JOB TYPE BY CODE NOT FOUND (JPA) ===");
        System.out.println("Code: " + code);
        System.out.println("Exception: " + exception.getClass().getSimpleName());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("============================================");
        
        verify(jobTypeRepository).findByCode(code);
    }

    @Test
    void deleteJobType_Success() {
        // Given
        Long id = 1L;
        when(jobTypeRepository.existsById(id)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> jobTypeJpaService.deleteJobType(id));

        // Then
        System.out.println("=== DELETE JOB TYPE SUCCESS (JPA) ===");
        System.out.println("ID: " + id);
        System.out.println("Status: Successfully deleted");
        System.out.println("=====================================");
        
        verify(jobTypeRepository).existsById(id);
        verify(jobTypeRepository).deleteById(id);
    }

    @Test
    void deleteJobType_NotFound_ThrowsException() {
        // Given
        Long id = 999L;
        when(jobTypeRepository.existsById(id)).thenReturn(false);

        // When & Then
        JobTypeNotFoundException exception = assertThrows(JobTypeNotFoundException.class,
                () -> jobTypeJpaService.deleteJobType(id));

        assertEquals("Job type with ID 999 not found", exception.getMessage());
        
        System.out.println("=== DELETE JOB TYPE NOT FOUND (JPA) ===");
        System.out.println("ID: " + id);
        System.out.println("Exception: " + exception.getClass().getSimpleName());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("=======================================");
        
        verify(jobTypeRepository).existsById(id);
        verify(jobTypeRepository, never()).deleteById(any());
    }

    @Test
    void deleteJobTypeByCode_Success() {
        // Given
        String code = "FULL_TIME";
        when(jobTypeRepository.existsByCode(code)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> jobTypeJpaService.deleteJobTypeByCode(code));

        // Then
        System.out.println("=== DELETE JOB TYPE BY CODE SUCCESS (JPA) ===");
        System.out.println("Code: " + code);
        System.out.println("Status: Successfully deleted");
        System.out.println("=============================================");
        
        verify(jobTypeRepository).existsByCode(code);
        verify(jobTypeRepository).deleteByCode(code);
    }

    @Test
    void deleteJobTypeByCode_NotFound_ThrowsException() {
        // Given
        String code = "INVALID_CODE";
        when(jobTypeRepository.existsByCode(code)).thenReturn(false);

        // When & Then
        JobTypeNotFoundException exception = assertThrows(JobTypeNotFoundException.class,
                () -> jobTypeJpaService.deleteJobTypeByCode(code));

        assertEquals("Job type with code 'INVALID_CODE' not found", exception.getMessage());
        
        System.out.println("=== DELETE JOB TYPE BY CODE NOT FOUND (JPA) ===");
        System.out.println("Code: " + code);
        System.out.println("Exception: " + exception.getClass().getSimpleName());
        System.out.println("Message: " + exception.getMessage());
        System.out.println("==============================================");
        
        verify(jobTypeRepository).existsByCode(code);
        verify(jobTypeRepository, never()).deleteByCode(any());
    }

    @Test
    void getAllJobTypes_Success() {
        // Given
        List<PJobType> jobTypes = Arrays.asList(testJobType);
        when(jobTypeRepository.findAll()).thenReturn(jobTypes);

        // When
        List<JobTypeResponse> result = jobTypeJpaService.getAllJobTypes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getPJobTypeId());
        assertEquals("FULL_TIME", result.get(0).getCode());
        
        System.out.println("=== GET ALL JOB TYPES SUCCESS (JPA) ===");
        System.out.println("Count: " + result.size());
        System.out.println("First item: " + result.get(0));
        System.out.println("======================================");
        
        verify(jobTypeRepository).findAll();
    }

    @Test
    void getAllJobTypesOrderByUpdateDateDesc_Success() {
        // Given
        List<PJobType> jobTypes = Arrays.asList(testJobType);
        when(jobTypeRepository.findAllOrderByUpdateDateDesc()).thenReturn(jobTypes);

        // When
        List<JobTypeResponse> result = jobTypeJpaService.getAllJobTypesOrderByUpdateDateDesc();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FULL_TIME", result.get(0).getCode());
        
        System.out.println("=== GET ALL JOB TYPES ORDER BY UPDATE DATE DESC SUCCESS (JPA) ===");
        System.out.println("Count: " + result.size());
        System.out.println("First item: " + result.get(0));
        System.out.println("================================================================");
        
        verify(jobTypeRepository).findAllOrderByUpdateDateDesc();
    }

    @Test
    void getAllJobTypesOrderByCodeAsc_Success() {
        // Given
        List<PJobType> jobTypes = Arrays.asList(testJobType);
        when(jobTypeRepository.findAllOrderByCodeAsc()).thenReturn(jobTypes);

        // When
        List<JobTypeResponse> result = jobTypeJpaService.getAllJobTypesOrderByCodeAsc();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FULL_TIME", result.get(0).getCode());
        
        System.out.println("=== GET ALL JOB TYPES ORDER BY CODE ASC SUCCESS (JPA) ===");
        System.out.println("Count: " + result.size());
        System.out.println("First item: " + result.get(0));
        System.out.println("========================================================");
        
        verify(jobTypeRepository).findAllOrderByCodeAsc();
    }

    @Test
    void searchJobTypesByDescription_Success() {
        // Given
        String description = "employment";
        List<PJobType> jobTypes = Arrays.asList(testJobType);
        when(jobTypeRepository.findByDescriptionContainingIgnoreCase(description)).thenReturn(jobTypes);

        // When
        List<JobTypeResponse> result = jobTypeJpaService.searchJobTypesByDescription(description);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FULL_TIME", result.get(0).getCode());
        
        System.out.println("=== SEARCH JOB TYPES BY DESCRIPTION SUCCESS (JPA) ===");
        System.out.println("Search term: " + description);
        System.out.println("Results count: " + result.size());
        System.out.println("First result: " + result.get(0));
        System.out.println("=====================================================");
        
        verify(jobTypeRepository).findByDescriptionContainingIgnoreCase(description);
    }

    @Test
    void getJobTypesByUpdateBy_Success() {
        // Given
        String updateBy = "admin";
        List<PJobType> jobTypes = Arrays.asList(testJobType);
        when(jobTypeRepository.findByUpdateBy(updateBy)).thenReturn(jobTypes);

        // When
        List<JobTypeResponse> result = jobTypeJpaService.getJobTypesByUpdateBy(updateBy);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FULL_TIME", result.get(0).getCode());
        assertEquals(updateBy, result.get(0).getUpdateBy());
        
        System.out.println("=== GET JOB TYPES BY UPDATE BY SUCCESS (JPA) ===");
        System.out.println("Update by: " + updateBy);
        System.out.println("Results count: " + result.size());
        System.out.println("First result: " + result.get(0));
        System.out.println("===============================================");
        
        verify(jobTypeRepository).findByUpdateBy(updateBy);
    }

    @Test
    void getJobTypesUpdatedAfter_Success() {
        // Given
        LocalDateTime updateDate = LocalDateTime.now().minusDays(1);
        List<PJobType> jobTypes = Arrays.asList(testJobType);
        when(jobTypeRepository.findByUpdateDateAfter(updateDate)).thenReturn(jobTypes);

        // When
        List<JobTypeResponse> result = jobTypeJpaService.getJobTypesUpdatedAfter(updateDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FULL_TIME", result.get(0).getCode());
        
        System.out.println("=== GET JOB TYPES UPDATED AFTER SUCCESS (JPA) ===");
        System.out.println("Update date: " + updateDate);
        System.out.println("Results count: " + result.size());
        System.out.println("First result: " + result.get(0));
        System.out.println("===============================================");
        
        verify(jobTypeRepository).findByUpdateDateAfter(updateDate);
    }

    @Test
    void getJobTypesUpdatedBetween_Success() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        List<PJobType> jobTypes = Arrays.asList(testJobType);
        when(jobTypeRepository.findByUpdateDateBetween(startDate, endDate)).thenReturn(jobTypes);

        // When
        List<JobTypeResponse> result = jobTypeJpaService.getJobTypesUpdatedBetween(startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FULL_TIME", result.get(0).getCode());
        
        System.out.println("=== GET JOB TYPES UPDATED BETWEEN SUCCESS (JPA) ===");
        System.out.println("Start date: " + startDate);
        System.out.println("End date: " + endDate);
        System.out.println("Results count: " + result.size());
        System.out.println("First result: " + result.get(0));
        System.out.println("=================================================");
        
        verify(jobTypeRepository).findByUpdateDateBetween(startDate, endDate);
    }

    @Test
    void existsById_Success() {
        // Given
        Long id = 1L;
        when(jobTypeRepository.existsById(id)).thenReturn(true);

        // When
        boolean exists = jobTypeJpaService.existsById(id);

        // Then
        assertTrue(exists);
        
        System.out.println("=== EXISTS BY ID SUCCESS (JPA) ===");
        System.out.println("ID: " + id);
        System.out.println("Exists: " + exists);
        System.out.println("================================");
        
        verify(jobTypeRepository).existsById(id);
    }

    @Test
    void existsByCode_Success() {
        // Given
        String code = "FULL_TIME";
        when(jobTypeRepository.existsByCode(code)).thenReturn(true);

        // When
        boolean exists = jobTypeJpaService.existsByCode(code);

        // Then
        assertTrue(exists);
        
        System.out.println("=== EXISTS BY CODE SUCCESS (JPA) ===");
        System.out.println("Code: " + code);
        System.out.println("Exists: " + exists);
        System.out.println("===================================");
        
        verify(jobTypeRepository).existsByCode(code);
    }

    @Test
    void countByUpdateBy_Success() {
        // Given
        String updateBy = "admin";
        when(jobTypeRepository.countByUpdateBy(updateBy)).thenReturn(3L);

        // When
        long count = jobTypeJpaService.countByUpdateBy(updateBy);

        // Then
        assertEquals(3L, count);
        
        System.out.println("=== COUNT BY UPDATE BY SUCCESS (JPA) ===");
        System.out.println("Update by: " + updateBy);
        System.out.println("Count: " + count);
        System.out.println("=======================================");
        
        verify(jobTypeRepository).countByUpdateBy(updateBy);
    }

    @Test
    void getTotalCount_Success() {
        // Given
        when(jobTypeRepository.count()).thenReturn(5L);

        // When
        long count = jobTypeJpaService.getTotalCount();

        // Then
        assertEquals(5L, count);
        
        System.out.println("=== GET TOTAL COUNT SUCCESS (JPA) ===");
        System.out.println("Total count: " + count);
        System.out.println("====================================");
        
        verify(jobTypeRepository).count();
    }
}
