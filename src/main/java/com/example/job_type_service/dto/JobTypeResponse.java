package com.example.job_type_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response object containing job type information")
public class JobTypeResponse {

    @Schema(description = "Unique identifier for the job type", example = "1")
    private Long pJobTypeId;
    
    @Schema(description = "Unique code for the job type", example = "FULL_TIME")
    private String code;
    
    @Schema(description = "Description of the job type", example = "Full-time employment position")
    private String description;
    
    @Schema(description = "Date and time when the job type was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updateDate;
    
    @Schema(description = "User who last updated the job type", example = "admin")
    private String updateBy;

    public JobTypeResponse() {}

    public JobTypeResponse(Long pJobTypeId, String code, String description, LocalDateTime updateDate, String updateBy) {
        this.pJobTypeId = pJobTypeId;
        this.code = code;
        this.description = description;
        this.updateDate = updateDate;
        this.updateBy = updateBy;
    }

    public static JobTypeResponseBuilder builder() {
        return new JobTypeResponseBuilder();
    }

    public static class JobTypeResponseBuilder {
        private Long pJobTypeId;
        private String code;
        private String description;
        private LocalDateTime updateDate;
        private String updateBy;

        public JobTypeResponseBuilder pJobTypeId(Long pJobTypeId) {
            this.pJobTypeId = pJobTypeId;
            return this;
        }

        public JobTypeResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public JobTypeResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public JobTypeResponseBuilder updateDate(LocalDateTime updateDate) {
            this.updateDate = updateDate;
            return this;
        }

        public JobTypeResponseBuilder updateBy(String updateBy) {
            this.updateBy = updateBy;
            return this;
        }

        public JobTypeResponse build() {
            return new JobTypeResponse(pJobTypeId, code, description, updateDate, updateBy);
        }
    }

    public Long getPJobTypeId() {
        return pJobTypeId;
    }

    public void setPJobTypeId(Long pJobTypeId) {
        this.pJobTypeId = pJobTypeId;
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

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
