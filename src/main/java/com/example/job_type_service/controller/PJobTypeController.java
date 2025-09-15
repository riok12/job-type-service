package com.example.job_type_service.controller;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import com.example.job_type_service.service.PJobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/job-types")
@CrossOrigin(origins = "*")
public class PJobTypeController {

    @Autowired
    private PJobTypeService pJobTypeService;

    @PostMapping
    public ResponseEntity<JobTypeResponse> createJobType(@Valid @RequestBody JobTypeRequest request) {
        try {
            JobTypeResponse response = pJobTypeService.insertJobType(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobTypeResponse> updateJobType(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobTypeRequest request) {
        try {
            JobTypeResponse response = pJobTypeService.updateJobType(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobTypeResponse> getJobType(@PathVariable Long id) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobType(@PathVariable Long id) {
        try {
            pJobTypeService.deleteJobType(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<java.util.List<JobTypeResponse>> getAllJobTypes() {
        try {
            java.util.List<JobTypeResponse> response = pJobTypeService.getAllJobTypes();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<JobTypeResponse> getJobTypeByCode(@PathVariable String code) {
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
