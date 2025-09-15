package com.example.job_type_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JobTypeRequest {

    @NotBlank(message = "Code is required")
    @Size(max = 128, message = "Code must not exceed 128 characters")
    private String code;

    @Size(max = 512, message = "Description must not exceed 512 characters")
    private String description;

    @NotBlank(message = "Update by is required")
    @Size(max = 128, message = "Update by must not exceed 128 characters")
    private String updateBy;

    // Constructors
    public JobTypeRequest() {}

    public JobTypeRequest(String code, String description, String updateBy) {
        this.code = code;
        this.description = description;
        this.updateBy = updateBy;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
