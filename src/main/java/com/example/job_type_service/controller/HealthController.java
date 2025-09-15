package com.example.job_type_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            try (Connection connection = dataSource.getConnection()) {
                response.put("status", "UP");
                response.put("database", "Connected to Oracle XE");
                response.put("connection", connection.getMetaData().getDatabaseProductName());
            }
        } catch (SQLException e) {
            response.put("status", "DOWN");
            response.put("error", e.getMessage());
        }
        
        return response;
    }
}
