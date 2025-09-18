package com.example.job_type_service.controller;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import com.example.job_type_service.exception.GlobalExceptionHandler;
import com.example.job_type_service.exception.JobTypeNotFoundException;
import com.example.job_type_service.exception.JobTypeServiceException;
import com.example.job_type_service.service.PJobTypeJpaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PJobTypeJpaControllerTest {

    @Mock
    private PJobTypeJpaService pJobTypeJpaService;

    @InjectMocks
    private PJobTypeJpaController pJobTypeJpaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pJobTypeJpaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createJobType_Success() throws Exception {
        // Given
        JobTypeRequest request = new JobTypeRequest("FULL_TIME", "Full-time employment", "admin");
        JobTypeResponse response = JobTypeResponse.builder()
                .pJobTypeId(1L)
                .code("FULL_TIME")
                .description("Full-time employment")
                .updateDate(LocalDateTime.now())
                .updateBy("admin")
                .build();

        doReturn(response).when(pJobTypeJpaService).insertJobType(any(JobTypeRequest.class));

        // When & Then
        mockMvc.perform(post("/api/v2/job-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pjobTypeId").value(1L))
                .andExpect(jsonPath("$.code").value("FULL_TIME"))
                .andExpect(jsonPath("$.description").value("Full-time employment"))
                .andExpect(jsonPath("$.updateBy").value("admin"));

        verify(pJobTypeJpaService).insertJobType(any(JobTypeRequest.class));
    }

    @Test
    void createJobType_CodeAlreadyExists_ReturnsBadRequest() throws Exception {
        // Given
        JobTypeRequest request = new JobTypeRequest("FULL_TIME", "Full-time employment", "admin");
        doThrow(new JobTypeServiceException("Job type with code 'FULL_TIME' already exists"))
                .when(pJobTypeJpaService).insertJobType(any(JobTypeRequest.class));

        // When & Then
        mockMvc.perform(post("/api/v2/job-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(pJobTypeJpaService).insertJobType(any(JobTypeRequest.class));
    }

    @Test
    void updateJobType_Success() throws Exception {
        // Given
        Long id = 1L;
        UpdateJobTypeRequest request = new UpdateJobTypeRequest("PART_TIME", "Part-time employment", "admin");
        JobTypeResponse response = JobTypeResponse.builder()
                .pJobTypeId(id)
                .code("PART_TIME")
                .description("Part-time employment")
                .updateDate(LocalDateTime.now())
                .updateBy("admin")
                .build();

        when(pJobTypeJpaService.updateJobType(eq(id), any(UpdateJobTypeRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/v2/job-types/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pjobTypeId").value(id))
                .andExpect(jsonPath("$.code").value("PART_TIME"))
                .andExpect(jsonPath("$.description").value("Part-time employment"))
                .andExpect(jsonPath("$.updateBy").value("admin"));

        verify(pJobTypeJpaService).updateJobType(eq(id), any(UpdateJobTypeRequest.class));
    }

    @Test
    void updateJobType_NotFound_ReturnsNotFound() throws Exception {
        // Given
        Long id = 999L;
        UpdateJobTypeRequest request = new UpdateJobTypeRequest("PART_TIME", "Part-time employment", "admin");
        when(pJobTypeJpaService.updateJobType(eq(id), any(UpdateJobTypeRequest.class)))
                .thenThrow(new JobTypeNotFoundException("Job type with ID 999 not found"));

        // When & Then
        mockMvc.perform(put("/api/v2/job-types/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).updateJobType(eq(id), any(UpdateJobTypeRequest.class));
    }

    @Test
    void getJobTypeById_Success() throws Exception {
        // Given
        Long id = 1L;
        JobTypeResponse response = JobTypeResponse.builder()
                .pJobTypeId(id)
                .code("FULL_TIME")
                .description("Full-time employment")
                .updateDate(LocalDateTime.now())
                .updateBy("admin")
                .build();

        when(pJobTypeJpaService.getJobTypeById(id)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pjobTypeId").value(id))
                .andExpect(jsonPath("$.code").value("FULL_TIME"))
                .andExpect(jsonPath("$.description").value("Full-time employment"))
                .andExpect(jsonPath("$.updateBy").value("admin"));

        verify(pJobTypeJpaService).getJobTypeById(id);
    }

    @Test
    void getJobTypeById_NotFound_ReturnsNotFound() throws Exception {
        // Given
        Long id = 999L;
        when(pJobTypeJpaService.getJobTypeById(id))
                .thenThrow(new JobTypeNotFoundException("Job type with ID 999 not found"));

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/{id}", id))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).getJobTypeById(id);
    }

    @Test
    void getJobTypeByCode_Success() throws Exception {
        // Given
        String code = "FULL_TIME";
        JobTypeResponse response = JobTypeResponse.builder()
                .pJobTypeId(1L)
                .code(code)
                .description("Full-time employment")
                .updateDate(LocalDateTime.now())
                .updateBy("admin")
                .build();

        when(pJobTypeJpaService.getJobTypeByCode(code)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/code/{code}", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pjobTypeId").value(1L))
                .andExpect(jsonPath("$.code").value(code))
                .andExpect(jsonPath("$.description").value("Full-time employment"))
                .andExpect(jsonPath("$.updateBy").value("admin"));

        verify(pJobTypeJpaService).getJobTypeByCode(code);
    }

    @Test
    void getJobTypeByCode_NotFound_ReturnsNotFound() throws Exception {
        // Given
        String code = "INVALID_CODE";
        when(pJobTypeJpaService.getJobTypeByCode(code))
                .thenThrow(new JobTypeNotFoundException("Job type with code 'INVALID_CODE' not found"));

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/code/{code}", code))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).getJobTypeByCode(code);
    }

    @Test
    void deleteJobType_Success() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(pJobTypeJpaService).deleteJobType(id);

        // When & Then
        mockMvc.perform(delete("/api/v2/job-types/{id}", id))
                .andExpect(status().isNoContent());

        verify(pJobTypeJpaService).deleteJobType(id);
    }

    @Test
    void deleteJobType_NotFound_ReturnsNotFound() throws Exception {
        // Given
        Long id = 999L;
        doThrow(new JobTypeNotFoundException("Job type with ID 999 not found"))
                .when(pJobTypeJpaService).deleteJobType(id);

        // When & Then
        mockMvc.perform(delete("/api/v2/job-types/{id}", id))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).deleteJobType(id);
    }

    @Test
    void deleteJobTypeByCode_Success() throws Exception {
        // Given
        String code = "FULL_TIME";
        doNothing().when(pJobTypeJpaService).deleteJobTypeByCode(code);

        // When & Then
        mockMvc.perform(delete("/api/v2/job-types/code/{code}", code))
                .andExpect(status().isNoContent());

        verify(pJobTypeJpaService).deleteJobTypeByCode(code);
    }

    @Test
    void deleteJobTypeByCode_NotFound_ReturnsNotFound() throws Exception {
        // Given
        String code = "INVALID_CODE";
        doThrow(new JobTypeNotFoundException("Job type with code 'INVALID_CODE' not found"))
                .when(pJobTypeJpaService).deleteJobTypeByCode(code);

        // When & Then
        mockMvc.perform(delete("/api/v2/job-types/code/{code}", code))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).deleteJobTypeByCode(code);
    }

    @Test
    void getAllJobTypes_Success() throws Exception {
        // Given
        List<JobTypeResponse> responses = Arrays.asList(
                JobTypeResponse.builder()
                        .pJobTypeId(1L)
                        .code("FULL_TIME")
                        .description("Full-time employment")
                        .updateDate(LocalDateTime.now())
                        .updateBy("admin")
                        .build(),
                JobTypeResponse.builder()
                        .pJobTypeId(2L)
                        .code("PART_TIME")
                        .description("Part-time employment")
                        .updateDate(LocalDateTime.now())
                        .updateBy("admin")
                        .build()
        );

        when(pJobTypeJpaService.getAllJobTypes()).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].pjobTypeId").value(1L))
                .andExpect(jsonPath("$[0].code").value("FULL_TIME"))
                .andExpect(jsonPath("$[1].pjobTypeId").value(2L))
                .andExpect(jsonPath("$[1].code").value("PART_TIME"));

        verify(pJobTypeJpaService).getAllJobTypes();
    }

    @Test
    void getAllJobTypesOrderByUpdateDateDesc_Success() throws Exception {
        // Given
        List<JobTypeResponse> responses = Arrays.asList(
                JobTypeResponse.builder()
                        .pJobTypeId(2L)
                        .code("PART_TIME")
                        .description("Part-time employment")
                        .updateDate(LocalDateTime.now())
                        .updateBy("admin")
                        .build(),
                JobTypeResponse.builder()
                        .pJobTypeId(1L)
                        .code("FULL_TIME")
                        .description("Full-time employment")
                        .updateDate(LocalDateTime.now().minusDays(1))
                        .updateBy("admin")
                        .build()
        );

        when(pJobTypeJpaService.getAllJobTypesOrderByUpdateDateDesc()).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/ordered/update-date-desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("PART_TIME"))
                .andExpect(jsonPath("$[1].code").value("FULL_TIME"));

        verify(pJobTypeJpaService).getAllJobTypesOrderByUpdateDateDesc();
    }

    @Test
    void getAllJobTypesOrderByCodeAsc_Success() throws Exception {
        // Given
        List<JobTypeResponse> responses = Arrays.asList(
                JobTypeResponse.builder()
                        .pJobTypeId(1L)
                        .code("FULL_TIME")
                        .description("Full-time employment")
                        .updateDate(LocalDateTime.now())
                        .updateBy("admin")
                        .build(),
                JobTypeResponse.builder()
                        .pJobTypeId(2L)
                        .code("PART_TIME")
                        .description("Part-time employment")
                        .updateDate(LocalDateTime.now())
                        .updateBy("admin")
                        .build()
        );

        when(pJobTypeJpaService.getAllJobTypesOrderByCodeAsc()).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/ordered/code-asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("FULL_TIME"))
                .andExpect(jsonPath("$[1].code").value("PART_TIME"));

        verify(pJobTypeJpaService).getAllJobTypesOrderByCodeAsc();
    }

    @Test
    void searchJobTypesByDescription_Success() throws Exception {
        // Given
        String description = "employment";
        List<JobTypeResponse> responses = Arrays.asList(
                JobTypeResponse.builder()
                        .pJobTypeId(1L)
                        .code("FULL_TIME")
                        .description("Full-time employment")
                        .updateDate(LocalDateTime.now())
                        .updateBy("admin")
                        .build()
        );

        when(pJobTypeJpaService.searchJobTypesByDescription(description)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/search/description")
                        .param("description", description))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("FULL_TIME"))
                .andExpect(jsonPath("$[0].description").value("Full-time employment"));

        verify(pJobTypeJpaService).searchJobTypesByDescription(description);
    }

    @Test
    void getJobTypesByUpdateBy_Success() throws Exception {
        // Given
        String updateBy = "admin";
        List<JobTypeResponse> responses = Arrays.asList(
                JobTypeResponse.builder()
                        .pJobTypeId(1L)
                        .code("FULL_TIME")
                        .description("Full-time employment")
                        .updateDate(LocalDateTime.now())
                        .updateBy(updateBy)
                        .build()
        );

        when(pJobTypeJpaService.getJobTypesByUpdateBy(updateBy)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/filter/update-by")
                        .param("updateBy", updateBy))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("FULL_TIME"))
                .andExpect(jsonPath("$[0].updateBy").value(updateBy));

        verify(pJobTypeJpaService).getJobTypesByUpdateBy(updateBy);
    }

    @Test
    void getJobTypesUpdatedAfter_Success() throws Exception {
        // Given
        LocalDateTime updateDate = LocalDateTime.now().minusDays(1);
        List<JobTypeResponse> responses = Arrays.asList(
                JobTypeResponse.builder()
                        .pJobTypeId(1L)
                        .code("FULL_TIME")
                        .description("Full-time employment")
                        .updateDate(LocalDateTime.now())
                        .updateBy("admin")
                        .build()
        );

        when(pJobTypeJpaService.getJobTypesUpdatedAfter(updateDate)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/filter/updated-after")
                        .param("updateDate", updateDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("FULL_TIME"));

        verify(pJobTypeJpaService).getJobTypesUpdatedAfter(updateDate);
    }

    @Test
    void getJobTypesUpdatedBetween_Success() throws Exception {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        List<JobTypeResponse> responses = Arrays.asList(
                JobTypeResponse.builder()
                        .pJobTypeId(1L)
                        .code("FULL_TIME")
                        .description("Full-time employment")
                        .updateDate(LocalDateTime.now())
                        .updateBy("admin")
                        .build()
        );

        when(pJobTypeJpaService.getJobTypesUpdatedBetween(startDate, endDate)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/filter/updated-between")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("FULL_TIME"));

        verify(pJobTypeJpaService).getJobTypesUpdatedBetween(startDate, endDate);
    }

    @Test
    void existsById_Success() throws Exception {
        // Given
        Long id = 1L;
        when(pJobTypeJpaService.existsById(id)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/exists/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(pJobTypeJpaService).existsById(id);
    }

    @Test
    void existsByCode_Success() throws Exception {
        // Given
        String code = "FULL_TIME";
        when(pJobTypeJpaService.existsByCode(code)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/exists/code/{code}", code))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(pJobTypeJpaService).existsByCode(code);
    }

    @Test
    void getTotalCount_Success() throws Exception {
        // Given
        when(pJobTypeJpaService.getTotalCount()).thenReturn(5L);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(pJobTypeJpaService).getTotalCount();
    }

    @Test
    void countByUpdateBy_Success() throws Exception {
        // Given
        String updateBy = "admin";
        when(pJobTypeJpaService.countByUpdateBy(updateBy)).thenReturn(3L);

        // When & Then
        mockMvc.perform(get("/api/v2/job-types/count/update-by")
                        .param("updateBy", updateBy))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(pJobTypeJpaService).countByUpdateBy(updateBy);
    }
}
