package com.example.job_type_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
@Tag(name = "Database Information", description = "Database information and status endpoints")
public class DatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/info")
    @Operation(summary = "Get database information", description = "Retrieves information about the database including version, table existence, and record counts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database information retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Database connection or query error")
    })
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> info = new HashMap<>();
        
        try {
            String version = jdbcTemplate.queryForObject("SELECT banner FROM v$version WHERE rownum = 1", String.class);
            info.put("version", version);
            
            List<Map<String, Object>> tables = jdbcTemplate.queryForList(
                "SELECT table_name FROM user_tables WHERE table_name = 'P_JOB_TYPE'");
            info.put("p_job_type_table_exists", !tables.isEmpty());
            
            List<Map<String, Object>> sequences = jdbcTemplate.queryForList(
                "SELECT sequence_name FROM user_sequences WHERE sequence_name = 'SEQ_P_JOB_TYPE'");
            info.put("seq_p_job_type_exists", !sequences.isEmpty());
            
            List<Map<String, Object>> packages = jdbcTemplate.queryForList(
                "SELECT object_name FROM user_objects WHERE object_type = 'PACKAGE' AND object_name = 'PACK_TEST'");
            info.put("pack_test_exists", !packages.isEmpty());
            
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM P_JOB_TYPE", Integer.class);
            info.put("total_job_types", count);
            
            info.put("status", "success");
            
        } catch (Exception e) {
            info.put("status", "error");
            info.put("error", e.getMessage());
        }
        
        return info;
    }
}
