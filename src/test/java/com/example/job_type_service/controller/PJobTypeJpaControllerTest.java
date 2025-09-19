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

        JobTypeRequest request = new JobTypeRequest("FULL_TIME", "Full-time employment", "admin");
        JobTypeResponse response = JobTypeResponse.builder()
                .pJobTypeId(1L)
                .code("FULL_TIME")
                .description("Full-time employment")
                .updateDate(LocalDateTime.now())
                .updateBy("admin")
                .build();

        doReturn(response).when(pJobTypeJpaService).insertJobType(any(JobTypeRequest.class));
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
        JobTypeRequest request = new JobTypeRequest("FULL_TIME", "Full-time employment", "admin");
        doThrow(new JobTypeServiceException("Job type with code 'FULL_TIME' already exists"))
                .when(pJobTypeJpaService).insertJobType(any(JobTypeRequest.class));

        mockMvc.perform(post("/api/v2/job-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(pJobTypeJpaService).insertJobType(any(JobTypeRequest.class));
    }

    @Test
    void updateJobType_Success() throws Exception {
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
        Long id = 999L;
        UpdateJobTypeRequest request = new UpdateJobTypeRequest("PART_TIME", "Part-time employment", "admin");
        when(pJobTypeJpaService.updateJobType(eq(id), any(UpdateJobTypeRequest.class)))
                .thenThrow(new JobTypeNotFoundException("Job type with ID 999 not found"));

        mockMvc.perform(put("/api/v2/job-types/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).updateJobType(eq(id), any(UpdateJobTypeRequest.class));
    }

    @Test
    void getJobTypeById_Success() throws Exception {
        Long id = 1L;
        JobTypeResponse response = JobTypeResponse.builder()
                .pJobTypeId(id)
                .code("FULL_TIME")
                .description("Full-time employment")
                .updateDate(LocalDateTime.now())
                .updateBy("admin")
                .build();

        when(pJobTypeJpaService.getJobTypeById(id)).thenReturn(response);
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
        Long id = 999L;
        when(pJobTypeJpaService.getJobTypeById(id))
                .thenThrow(new JobTypeNotFoundException("Job type with ID 999 not found"));

        mockMvc.perform(get("/api/v2/job-types/{id}", id))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).getJobTypeById(id);
    }

    @Test
    void getJobTypeByCode_Success() throws Exception {
        String code = "FULL_TIME";
        JobTypeResponse response = JobTypeResponse.builder()
                .pJobTypeId(1L)
                .code(code)
                .description("Full-time employment")
                .updateDate(LocalDateTime.now())
                .updateBy("admin")
                .build();

        when(pJobTypeJpaService.getJobTypeByCode(code)).thenReturn(response);
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
        String code = "INVALID_CODE";
        when(pJobTypeJpaService.getJobTypeByCode(code))
                .thenThrow(new JobTypeNotFoundException("Job type with code 'INVALID_CODE' not found"));

        mockMvc.perform(get("/api/v2/job-types/code/{code}", code))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).getJobTypeByCode(code);
    }

    @Test
    void deleteJobType_Success() throws Exception {
        Long id = 1L;
        doNothing().when(pJobTypeJpaService).deleteJobType(id);
        mockMvc.perform(delete("/api/v2/job-types/{id}", id))
                .andExpect(status().isNoContent());

        verify(pJobTypeJpaService).deleteJobType(id);
    }

    @Test
    void deleteJobType_NotFound_ReturnsNotFound() throws Exception {
        Long id = 999L;
        doThrow(new JobTypeNotFoundException("Job type with ID 999 not found"))
                .when(pJobTypeJpaService).deleteJobType(id);
        mockMvc.perform(delete("/api/v2/job-types/{id}", id))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).deleteJobType(id);
    }

    @Test
    void deleteJobTypeByCode_Success() throws Exception {
        String code = "FULL_TIME";
        doNothing().when(pJobTypeJpaService).deleteJobTypeByCode(code);
        mockMvc.perform(delete("/api/v2/job-types/code/{code}", code))
                .andExpect(status().isNoContent());

        verify(pJobTypeJpaService).deleteJobTypeByCode(code);
    }

    @Test
    void deleteJobTypeByCode_NotFound_ReturnsNotFound() throws Exception {
        String code = "INVALID_CODE";
        doThrow(new JobTypeNotFoundException("Job type with code 'INVALID_CODE' not found"))
                .when(pJobTypeJpaService).deleteJobTypeByCode(code);
        mockMvc.perform(delete("/api/v2/job-types/code/{code}", code))
                .andExpect(status().isNotFound());

        verify(pJobTypeJpaService).deleteJobTypeByCode(code);
    }

    @Test
    void getAllJobTypes_Success() throws Exception {
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


}
