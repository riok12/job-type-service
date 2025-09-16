package com.example.job_type_service.controller;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import com.example.job_type_service.exception.GlobalExceptionHandler;
import com.example.job_type_service.exception.JobTypeNotFoundException;
import com.example.job_type_service.exception.JobTypeServiceException;
import com.example.job_type_service.service.PJobTypeService;
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
class PJobTypeControllerBasicTest {

    @Mock
    private PJobTypeService pJobTypeService;

    @InjectMocks
    private PJobTypeController pJobTypeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pJobTypeController)
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

        when(pJobTypeService.insertJobType(any(JobTypeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/job-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("=== CREATE JOB TYPE SUCCESS ===");
                    System.out.println("Request: " + objectMapper.writeValueAsString(request));
                    System.out.println("Response: " + result.getResponse().getContentAsString());
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("===============================");
                });

        verify(pJobTypeService).insertJobType(any(JobTypeRequest.class));
    }

    @Test
    void createJobType_ServiceException() throws Exception {
        JobTypeRequest request = new JobTypeRequest("FULL_TIME", "Full-time employment", "admin");
        when(pJobTypeService.insertJobType(any(JobTypeRequest.class)))
                .thenThrow(new JobTypeServiceException("Database error"));

        mockMvc.perform(post("/api/job-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("=== CREATE JOB TYPE ERROR ===");
                    System.out.println("Request: " + objectMapper.writeValueAsString(request));
                    System.out.println("Response: " + result.getResponse().getContentAsString());
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("=============================");
                });

        verify(pJobTypeService).insertJobType(any(JobTypeRequest.class));
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

        when(pJobTypeService.updateJobType(eq(id), any(UpdateJobTypeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/job-types/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("=== UPDATE JOB TYPE SUCCESS ===");
                    System.out.println("ID: " + id);
                    System.out.println("Request: " + objectMapper.writeValueAsString(request));
                    System.out.println("Response: " + result.getResponse().getContentAsString());
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("===============================");
                });

        verify(pJobTypeService).updateJobType(eq(id), any(UpdateJobTypeRequest.class));
    }

    @Test
    void updateJobType_NotFound() throws Exception {
        Long id = 999L;
        UpdateJobTypeRequest request = new UpdateJobTypeRequest("PART_TIME", "Part-time employment", "admin");
        when(pJobTypeService.updateJobType(eq(id), any(UpdateJobTypeRequest.class)))
                .thenThrow(new JobTypeNotFoundException("Job type with ID 999 not found"));

        mockMvc.perform(put("/api/job-types/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(pJobTypeService).updateJobType(eq(id), any(UpdateJobTypeRequest.class));
    }

    @Test
    void getJobType_Success() throws Exception {
        Long id = 1L;
        JobTypeResponse response = JobTypeResponse.builder()
                .pJobTypeId(id)
                .code("FULL_TIME")
                .description("Full-time employment")
                .updateDate(LocalDateTime.now())
                .updateBy("admin")
                .build();

        when(pJobTypeService.getJobTypeById(id)).thenReturn(response);

        mockMvc.perform(get("/api/job-types/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("=== GET JOB TYPE SUCCESS ===");
                    System.out.println("ID: " + id);
                    System.out.println("Response: " + result.getResponse().getContentAsString());
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("=============================");
                });

        verify(pJobTypeService).getJobTypeById(id);
    }

    @Test
    void getJobType_NotFound() throws Exception {
        Long id = 999L;
        when(pJobTypeService.getJobTypeById(id))
                .thenThrow(new JobTypeNotFoundException("Job type with ID 999 not found"));

        mockMvc.perform(get("/api/job-types/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(pJobTypeService).getJobTypeById(id);
    }

    @Test
    void deleteJobType_Success() throws Exception {
        Long id = 1L;
        doNothing().when(pJobTypeService).deleteJobType(id);

        mockMvc.perform(delete("/api/job-types/{id}", id))
                .andExpect(status().isNoContent());

        verify(pJobTypeService).deleteJobType(id);
    }

    @Test
    void deleteJobType_NotFound() throws Exception {
        Long id = 999L;
        doThrow(new JobTypeNotFoundException("Job type with ID 999 not found"))
                .when(pJobTypeService).deleteJobType(id);

        mockMvc.perform(delete("/api/job-types/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(pJobTypeService).deleteJobType(id);
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

        when(pJobTypeService.getAllJobTypes()).thenReturn(responses);

        mockMvc.perform(get("/api/job-types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("=== GET ALL JOB TYPES SUCCESS ===");
                    System.out.println("Response: " + result.getResponse().getContentAsString());
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("==================================");
                });

        verify(pJobTypeService).getAllJobTypes();
    }

    @Test
    void getAllJobTypes_ServiceException() throws Exception {
        when(pJobTypeService.getAllJobTypes())
                .thenThrow(new JobTypeServiceException("Database error"));

        mockMvc.perform(get("/api/job-types"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(pJobTypeService).getAllJobTypes();
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

        when(pJobTypeService.getJobTypeByCode(code)).thenReturn(response);

        mockMvc.perform(get("/api/job-types/code/{code}", code))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(pJobTypeService).getJobTypeByCode(code);
    }

    @Test
    void getJobTypeByCode_NotFound() throws Exception {
        String code = "INVALID_CODE";
        when(pJobTypeService.getJobTypeByCode(code))
                .thenThrow(new JobTypeNotFoundException("Job type with code INVALID_CODE not found"));

        mockMvc.perform(get("/api/job-types/code/{code}", code))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(pJobTypeService).getJobTypeByCode(code);
    }
}
