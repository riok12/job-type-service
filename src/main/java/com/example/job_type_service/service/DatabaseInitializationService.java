package com.example.job_type_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class DatabaseInitializationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initializeDatabase() {
        try {
            executeSqlScript("sql/create_table.sql");
            
            executeSqlScript("sql/create_package.sql");
            
            executeSqlScript("sql/create_package_body.sql");
            
            System.out.println("Database initialization completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during database initialization: " + e.getMessage());
        }
    }

    private void executeSqlScript(String scriptPath) throws IOException {
        ClassPathResource resource = new ClassPathResource(scriptPath);
        String sql = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);

        if (scriptPath.contains("package")) {
            try {
                jdbcTemplate.execute(sql);
            } catch (Exception e) {
                System.out.println("Package execution warning: " + e.getMessage());
            }
        } else {
            String[] statements = sql.split(";");
            for (String statement : statements) {
                String trimmedStatement = statement.trim();
                if (!trimmedStatement.isEmpty()) {
                    try {
                        jdbcTemplate.execute(trimmedStatement);
                    } catch (Exception e) {
                        System.out.println("Statement execution warning: " + e.getMessage());
                    }
                }
            }
        }
    }
}
