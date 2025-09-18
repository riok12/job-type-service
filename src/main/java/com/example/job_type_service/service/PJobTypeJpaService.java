package com.example.job_type_service.service;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import com.example.job_type_service.entity.PJobType;
import com.example.job_type_service.exception.JobTypeNotFoundException;
import com.example.job_type_service.exception.JobTypeServiceException;
import com.example.job_type_service.repository.PJobTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PJobTypeJpaService {

    private final PJobTypeRepository jobTypeRepository;
    private final JdbcTemplate jdbcTemplate;

    public PJobTypeJpaService(PJobTypeRepository jobTypeRepository, JdbcTemplate jdbcTemplate) {
        this.jobTypeRepository = jobTypeRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public JobTypeResponse insertJobType(JobTypeRequest request) {
        try {
            if (jobTypeRepository.existsByCode(request.getCode())) {
                throw new JobTypeServiceException("Job type with code '" + request.getCode() + "' already exists");
            }

            Long newId = jdbcTemplate.queryForObject("SELECT SEQ_P_JOB_TYPE.NEXTVAL FROM dual", Long.class);

            PJobType jobType = new PJobType();
            jobType.setPJobTypeId(newId);
            jobType.setCode(request.getCode());
            jobType.setDescription(request.getDescription());
            jobType.setUpdateDate(LocalDateTime.now());
            jobType.setUpdateBy(request.getUpdateBy());

            PJobType savedJobType = jobTypeRepository.save(jobType);
            return convertToResponse(savedJobType);
        } catch (JobTypeServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to insert job type: " + e.getMessage(), e);
        }
    }

    public JobTypeResponse updateJobType(Long id, UpdateJobTypeRequest request) {
        try {
            Optional<PJobType> existingJobTypeOpt = jobTypeRepository.findById(id);
            if (existingJobTypeOpt.isEmpty()) {
                throw new JobTypeNotFoundException("Job type with ID " + id + " not found");
            }

            PJobType existingJobType = existingJobTypeOpt.get();

            if (!existingJobType.getCode().equals(request.getCode()) && 
                jobTypeRepository.existsByCode(request.getCode())) {
                throw new JobTypeServiceException("Job type with code '" + request.getCode() + "' already exists");
            }

            existingJobType.setCode(request.getCode());
            existingJobType.setDescription(request.getDescription());
            existingJobType.setUpdateDate(LocalDateTime.now());
            existingJobType.setUpdateBy(request.getUpdateBy());

            PJobType updatedJobType = jobTypeRepository.save(existingJobType);
            return convertToResponse(updatedJobType);
        } catch (JobTypeNotFoundException | JobTypeServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to update job type: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public JobTypeResponse getJobTypeById(Long id) {
        try {
            Optional<PJobType> jobTypeOpt = jobTypeRepository.findById(id);
            if (jobTypeOpt.isEmpty()) {
                throw new JobTypeNotFoundException("Job type with ID " + id + " not found");
            }
            return convertToResponse(jobTypeOpt.get());
        } catch (JobTypeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to retrieve job type: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public JobTypeResponse getJobTypeByCode(String code) {
        try {
            Optional<PJobType> jobTypeOpt = jobTypeRepository.findByCode(code);
            if (jobTypeOpt.isEmpty()) {
                throw new JobTypeNotFoundException("Job type with code '" + code + "' not found");
            }
            return convertToResponse(jobTypeOpt.get());
        } catch (JobTypeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to retrieve job type by code: " + e.getMessage(), e);
        }
    }

    public void deleteJobType(Long id) {
        try {
            if (!jobTypeRepository.existsById(id)) {
                throw new JobTypeNotFoundException("Job type with ID " + id + " not found");
            }
            jobTypeRepository.deleteById(id);
        } catch (JobTypeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to delete job type: " + e.getMessage(), e);
        }
    }

    public void deleteJobTypeByCode(String code) {
        try {
            if (!jobTypeRepository.existsByCode(code)) {
                throw new JobTypeNotFoundException("Job type with code '" + code + "' not found");
            }
            jobTypeRepository.deleteByCode(code);
        } catch (JobTypeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to delete job type by code: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<JobTypeResponse> getAllJobTypes() {
        try {
            List<PJobType> jobTypes = jobTypeRepository.findAll();
            return jobTypes.stream()
                    .map(this::convertToResponse)
                    .toList();
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to retrieve all job types: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<JobTypeResponse> getAllJobTypesOrderByUpdateDateDesc() {
        try {
            List<PJobType> jobTypes = jobTypeRepository.findAllOrderByUpdateDateDesc();
            return jobTypes.stream()
                    .map(this::convertToResponse)
                    .toList();
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to retrieve job types ordered by update date: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<JobTypeResponse> getAllJobTypesOrderByCodeAsc() {
        try {
            List<PJobType> jobTypes = jobTypeRepository.findAllOrderByCodeAsc();
            return jobTypes.stream()
                    .map(this::convertToResponse)
                    .toList();
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to retrieve job types ordered by code: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<JobTypeResponse> searchJobTypesByDescription(String description) {
        try {
            List<PJobType> jobTypes = jobTypeRepository.findByDescriptionContainingIgnoreCase(description);
            return jobTypes.stream()
                    .map(this::convertToResponse)
                    .toList();
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to search job types by description: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<JobTypeResponse> getJobTypesByUpdateBy(String updateBy) {
        try {
            List<PJobType> jobTypes = jobTypeRepository.findByUpdateBy(updateBy);
            return jobTypes.stream()
                    .map(this::convertToResponse)
                    .toList();
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to retrieve job types by update user: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<JobTypeResponse> getJobTypesUpdatedAfter(LocalDateTime updateDate) {
        try {
            List<PJobType> jobTypes = jobTypeRepository.findByUpdateDateAfter(updateDate);
            return jobTypes.stream()
                    .map(this::convertToResponse)
                    .toList();
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to retrieve job types updated after date: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<JobTypeResponse> getJobTypesUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<PJobType> jobTypes = jobTypeRepository.findByUpdateDateBetween(startDate, endDate);
            return jobTypes.stream()
                    .map(this::convertToResponse)
                    .toList();
        } catch (Exception e) {
            throw new JobTypeServiceException("Failed to retrieve job types updated between dates: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return jobTypeRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return jobTypeRepository.existsByCode(code);
    }

    @Transactional(readOnly = true)
    public long countByUpdateBy(String updateBy) {
        return jobTypeRepository.countByUpdateBy(updateBy);
    }

    @Transactional(readOnly = true)
    public long getTotalCount() {
        return jobTypeRepository.count();
    }

    private JobTypeResponse convertToResponse(PJobType jobType) {
        return JobTypeResponse.builder()
                .pJobTypeId(jobType.getPJobTypeId())
                .code(jobType.getCode())
                .description(jobType.getDescription())
                .updateDate(jobType.getUpdateDate())
                .updateBy(jobType.getUpdateBy())
                .build();
    }
}
