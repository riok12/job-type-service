package com.example.job_type_service.controller;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import com.example.job_type_service.service.PJobTypeJpaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/job-types")
@Tag(name = "Job Type JPA Controller", description = "Job Type management using JPA Repository")
public class PJobTypeJpaController {

    @Autowired
    private PJobTypeJpaService jobTypeJpaService;

    @PostMapping
    @Operation(summary = "Create a new job type", description = "Creates a new job type with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job type created successfully",
                    content = @Content(schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Job type with this code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobTypeResponse> createJobType(@Valid @RequestBody JobTypeRequest request) {
        JobTypeResponse response = jobTypeJpaService.insertJobType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a job type", description = "Updates an existing job type by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job type updated successfully",
                    content = @Content(schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Job type not found"),
            @ApiResponse(responseCode = "409", description = "Job type with this code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobTypeResponse> updateJobType(
            @Parameter(description = "Job type ID") @PathVariable Long id,
            @Valid @RequestBody UpdateJobTypeRequest request) {
        JobTypeResponse response = jobTypeJpaService.updateJobType(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job type by ID", description = "Retrieves a job type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job type found",
                    content = @Content(schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Job type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobTypeResponse> getJobTypeById(
            @Parameter(description = "Job type ID") @PathVariable Long id) {
        JobTypeResponse response = jobTypeJpaService.getJobTypeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get job type by code", description = "Retrieves a job type by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job type found",
                    content = @Content(schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Job type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobTypeResponse> getJobTypeByCode(
            @Parameter(description = "Job type code") @PathVariable String code) {
        JobTypeResponse response = jobTypeJpaService.getJobTypeByCode(code);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job type by ID", description = "Deletes a job type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Job type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteJobType(
            @Parameter(description = "Job type ID") @PathVariable Long id) {
        jobTypeJpaService.deleteJobType(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/code/{code}")
    @Operation(summary = "Delete job type by code", description = "Deletes a job type by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Job type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteJobTypeByCode(
            @Parameter(description = "Job type code") @PathVariable String code) {
        jobTypeJpaService.deleteJobTypeByCode(code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all job types", description = "Retrieves all job types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job types retrieved successfully",
                    content = @Content(schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<JobTypeResponse>> getAllJobTypes() {
        List<JobTypeResponse> responses = jobTypeJpaService.getAllJobTypes();
        return ResponseEntity.ok(responses);
    }

}
