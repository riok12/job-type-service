package com.example.job_type_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "P_JOB_TYPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PJobType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_P_JOB_TYPE")
    @SequenceGenerator(name = "SEQ_P_JOB_TYPE", sequenceName = "SEQ_P_JOB_TYPE", allocationSize = 1)
    @Column(name = "P_JOB_TYPE_ID")
    private Long pJobTypeId;

    @NotBlank(message = "Code cannot be blank")
    @Size(max = 128, message = "Code cannot exceed 128 characters")
    @Column(name = "CODE", nullable = false, length = 128, unique = true)
    private String code;

    @Size(max = 512, message = "Description cannot exceed 512 characters")
    @Column(name = "DESCRIPTION", length = 512)
    private String description;

    @NotNull(message = "Update date cannot be null")
    @Column(name = "UPDATE_DATE", nullable = false)
    private LocalDateTime updateDate;

    @NotBlank(message = "Update by cannot be blank")
    @Size(max = 128, message = "Update by cannot exceed 128 characters")
    @Column(name = "UPDATE_BY", nullable = false, length = 128)
    private String updateBy;

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
