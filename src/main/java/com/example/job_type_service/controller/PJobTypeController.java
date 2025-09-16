package com.example.job_type_service.controller;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import com.example.job_type_service.service.PJobTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/job-types")
@CrossOrigin(origins = "*")
@Tag(name = "Job Type Management", description = "APIs for managing job types")
public class PJobTypeController {

    @Autowired
    private PJobTypeService pJobTypeService;

    @Operation(summary = "Create a new job type", description = "Creates a new job type with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job type created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<JobTypeResponse> createJobType(
            @Parameter(description = "Job type information", required = true)
            @Valid @RequestBody JobTypeRequest request) {
        try {
            JobTypeResponse response = pJobTypeService.insertJobType(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update an existing job type", description = "Updates an existing job type with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job type updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Job type not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<JobTypeResponse> updateJobType(
            @Parameter(description = "Job type ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated job type information", required = true)
            @Valid @RequestBody UpdateJobTypeRequest request) {
        try {
            JobTypeResponse response = pJobTypeService.updateJobType(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Get job type by ID", description = "Retrieves a specific job type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job type found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Job type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<JobTypeResponse> getJobType(
            @Parameter(description = "Job type ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            JobTypeResponse response = pJobTypeService.getJobTypeById(id);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Delete a job type", description = "Deletes a job type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job type deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobType(
            @Parameter(description = "Job type ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            pJobTypeService.deleteJobType(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Get all job types", description = "Retrieves a list of all job types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job types retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<java.util.List<JobTypeResponse>> getAllJobTypes() {
        try {
            java.util.List<JobTypeResponse> response = pJobTypeService.getAllJobTypes();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get job type by code", description = "Retrieves a specific job type by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job type found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobTypeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Job type not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<JobTypeResponse> getJobTypeByCode(
            @Parameter(description = "Job type code", required = true, example = "FULL_TIME")
            @PathVariable String code) {
        try {
            JobTypeResponse response = pJobTypeService.getJobTypeByCode(code);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
