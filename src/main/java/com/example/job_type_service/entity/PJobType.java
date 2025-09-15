package com.example.job_type_service.entity;

import jakarta.persistence.*;
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
    @Column(name = "P_JOB_TYPE_ID")
    private Long pJobTypeId;

    @Column(name = "CODE", nullable = false, length = 128)
    private String code;

    @Column(name = "DESCRIPTION", length = 512)
    private String description;

    @Column(name = "UPDATE_DATE", nullable = false)
    private LocalDateTime updateDate;

    @Column(name = "UPDATE_BY", nullable = false, length = 128)
    private String updateBy;
}
