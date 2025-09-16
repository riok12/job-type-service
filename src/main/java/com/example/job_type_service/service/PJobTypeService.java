package com.example.job_type_service.service;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import com.example.job_type_service.exception.JobTypeNotFoundException;
import com.example.job_type_service.exception.JobTypeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


@Service
public class PJobTypeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JobTypeResponse insertJobType(JobTypeRequest request) {
        try {
            Long newId = jdbcTemplate.queryForObject("SELECT SEQ_P_JOB_TYPE.NEXTVAL FROM dual", Long.class);
            
            String sql = "{ call PACK_TEST.insert_job_type(?, ?, ?, ?) }";
            
            jdbcTemplate.execute(sql, (CallableStatement cs) -> {
                try {
                    cs.setLong(1, newId);
                    cs.setString(2, request.getCode());
                    cs.setString(3, request.getDescription());
                    cs.setString(4, request.getUpdateBy());
                    cs.execute();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to execute stored procedure: " + e.getMessage(), e);
                }
                return null;
            });
            
            return getJobTypeById(newId);
        } catch (Exception e) {
            if (e.getCause() instanceof SQLException) {
                throw new JobTypeServiceException("Failed to insert job type: " + e.getCause().getMessage(), e);
            }
            throw new JobTypeServiceException("Unexpected error while inserting job type: " + e.getMessage(), e);
        }
    }

    public JobTypeResponse updateJobType(Long id, UpdateJobTypeRequest request) {
        try {
            String sql = "{ call PACK_TEST.update_job_type(?, ?, ?, ?) }";
            
            jdbcTemplate.execute(sql, (CallableStatement cs) -> {
                try {
                    cs.setLong(1, id);
                    cs.setString(2, request.getCode());
                    cs.setString(3, request.getDescription());
                    cs.setString(4, request.getUpdateBy());
                    cs.execute();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 20001) {
                        throw new RuntimeException("Job type with ID " + id + " not found", e);
                    }
                    throw new RuntimeException("Failed to execute stored procedure: " + e.getMessage(), e);
                }
                return null;
            });
            
            return getJobTypeById(id);
        } catch (Exception e) {
            if (e.getCause() instanceof SQLException) {
                SQLException sqlEx = (SQLException) e.getCause();
                if (sqlEx.getErrorCode() == 20001) {
                    throw new JobTypeNotFoundException("Job type with ID " + id + " not found");
                }
                throw new JobTypeServiceException("Failed to update job type: " + sqlEx.getMessage(), e);
            }
            throw new JobTypeServiceException("Unexpected error while updating job type: " + e.getMessage(), e);
        }
    }

    public JobTypeResponse getJobTypeById(Long id) {
        try {
            String sql = "{ call PACK_TEST.view_job_type(?, ?, ?, ?, ?) }";
            
            return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
                try {
                    cs.setLong(1, id);
                    cs.registerOutParameter(2, Types.VARCHAR);
                    cs.registerOutParameter(3, Types.VARCHAR);
                    cs.registerOutParameter(4, Types.TIMESTAMP);
                    cs.registerOutParameter(5, Types.VARCHAR);
                    cs.execute();
                    
                    String code = cs.getString(2);
                    if (code == null) {
                        throw new RuntimeException("Job type with ID " + id + " not found");
                    }
                    
                    return JobTypeResponse.builder()
                            .pJobTypeId(id)
                            .code(code)
                            .description(cs.getString(3))
                            .updateDate(cs.getTimestamp(4).toLocalDateTime())
                            .updateBy(cs.getString(5))
                            .build();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to execute stored procedure: " + e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                throw new JobTypeNotFoundException("Job type with ID " + id + " not found");
            }
            if (e.getCause() instanceof SQLException) {
                throw new JobTypeServiceException("Failed to retrieve job type: " + e.getCause().getMessage(), e);
            }
            throw new JobTypeServiceException("Unexpected error while retrieving job type: " + e.getMessage(), e);
        }
    }

    public void deleteJobType(Long id) {
        try {
            String sql = "{ call PACK_TEST.delete_job_type(?) }";
            
            jdbcTemplate.execute(sql, (CallableStatement cs) -> {
                try {
                    cs.setLong(1, id);
                    cs.execute();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 20002) {
                        throw new RuntimeException("Job type with ID " + id + " not found", e);
                    }
                    throw new RuntimeException("Failed to execute stored procedure: " + e.getMessage(), e);
                }
                return null;
            });
        } catch (Exception e) {
            if (e.getCause() instanceof SQLException) {
                SQLException sqlEx = (SQLException) e.getCause();
                if (sqlEx.getErrorCode() == 20002) {
                    throw new JobTypeNotFoundException("Job type with ID " + id + " not found");
                }
                throw new JobTypeServiceException("Failed to delete job type: " + sqlEx.getMessage(), e);
            }
            throw new JobTypeServiceException("Unexpected error while deleting job type: " + e.getMessage(), e);
        }
    }

    public List<JobTypeResponse> getAllJobTypes() {
        try {
            String sql = "{ call PACK_TEST.get_all_job_types(?) }";
            
            return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
                try {
                    cs.registerOutParameter(1, Types.REF_CURSOR);
                    cs.execute();
                    
                    java.sql.ResultSet rs = (java.sql.ResultSet) cs.getObject(1);
                    List<JobTypeResponse> jobTypes = new ArrayList<>();
                    
                    while (rs.next()) {
                        jobTypes.add(JobTypeResponse.builder()
                            .pJobTypeId(rs.getLong("P_JOB_TYPE_ID"))
                            .code(rs.getString("CODE"))
                            .description(rs.getString("DESCRIPTION"))
                            .updateDate(rs.getTimestamp("UPDATE_DATE").toLocalDateTime())
                            .updateBy(rs.getString("UPDATE_BY"))
                            .build());
                    }
                    
                    rs.close();
                    return jobTypes;
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to execute stored procedure: " + e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            if (e.getCause() instanceof SQLException) {
                throw new JobTypeServiceException("Failed to retrieve all job types: " + e.getCause().getMessage(), e);
            }
            throw new JobTypeServiceException("Unexpected error while retrieving all job types: " + e.getMessage(), e);
        }
    }

    public JobTypeResponse getJobTypeByCode(String code) {
        try {
            String sql = "{ call PACK_TEST.get_job_type_by_code(?, ?, ?, ?, ?) }";
            
            return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
                try {
                    cs.setString(1, code);
                    cs.registerOutParameter(2, Types.NUMERIC);
                    cs.registerOutParameter(3, Types.VARCHAR);
                    cs.registerOutParameter(4, Types.TIMESTAMP);
                    cs.registerOutParameter(5, Types.VARCHAR);
                    cs.execute();
                    
                    Long id = cs.getLong(2);
                    if (id == null || id == 0) {
                        throw new RuntimeException("Job type with code " + code + " not found");
                    }
                    
                    return JobTypeResponse.builder()
                            .pJobTypeId(id)
                            .code(code)
                            .description(cs.getString(3))
                            .updateDate(cs.getTimestamp(4).toLocalDateTime())
                            .updateBy(cs.getString(5))
                            .build();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to execute stored procedure: " + e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                throw new JobTypeNotFoundException("Job type with code " + code + " not found");
            }
            if (e.getCause() instanceof SQLException) {
                throw new JobTypeServiceException("Failed to retrieve job type by code: " + e.getCause().getMessage(), e);
            }
            throw new JobTypeServiceException("Unexpected error while retrieving job type by code: " + e.getMessage(), e);
        }
    }

}
