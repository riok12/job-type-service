package com.example.job_type_service.dto;

import java.time.LocalDateTime;

public class JobTypeResponse {

    private Long pJobTypeId;
    private String code;
    private String description;
    private LocalDateTime updateDate;
    private String updateBy;

    // Constructors
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
