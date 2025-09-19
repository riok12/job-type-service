package com.example.job_type_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Health Check", description = "Health check endpoints for monitoring service status")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Checks the health status of the service and database connection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Health check completed successfully"),
            @ApiResponse(responseCode = "500", description = "Service or database connection issues")
    })
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
