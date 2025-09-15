package com.example.job_type_service.controller;

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
public class DatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/info")
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
