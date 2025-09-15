package com.example.job_type_service.service;

import com.example.job_type_service.dto.JobTypeRequest;
import com.example.job_type_service.dto.JobTypeResponse;
import com.example.job_type_service.dto.UpdateJobTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Types;


@Service
public class PJobTypeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JobTypeResponse insertJobType(JobTypeRequest request) {
        // Get next sequence value first
        Long newId = jdbcTemplate.queryForObject("SELECT SEQ_P_JOB_TYPE.NEXTVAL FROM dual", Long.class);
        
        String sql = "{ call PACK_TEST.insert_job_type(?, ?, ?, ?) }";
        
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setLong(1, newId);
            cs.setString(2, request.getCode());
            cs.setString(3, request.getDescription());
            cs.setString(4, request.getUpdateBy());
            cs.execute();
            return null;
        });
        
        return getJobTypeById(newId);
    }

    public JobTypeResponse updateJobType(Long id, UpdateJobTypeRequest request) {
        String sql = "{ call PACK_TEST.update_job_type(?, ?, ?, ?) }";
        
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setLong(1, id);
            cs.setString(2, request.getCode());
            cs.setString(3, request.getDescription());
            cs.setString(4, request.getUpdateBy());
            cs.execute();
            return null;
        });
        
        return getJobTypeById(id);
    }

    public JobTypeResponse getJobTypeById(Long id) {
        String sql = "{ call PACK_TEST.view_job_type(?, ?, ?, ?, ?) }";
        
        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setLong(1, id);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.registerOutParameter(4, Types.TIMESTAMP);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.execute();
            
            String code = cs.getString(2);
            if (code == null) {
                return null; // No data found
            }
            
            return JobTypeResponse.builder()
                    .pJobTypeId(id)
                    .code(code)
                    .description(cs.getString(3))
                    .updateDate(cs.getTimestamp(4).toLocalDateTime())
                    .updateBy(cs.getString(5))
                    .build();
        });
    }

    public void deleteJobType(Long id) {
        String sql = "{ call PACK_TEST.delete_job_type(?) }";
        
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setLong(1, id);
            cs.execute();
            return null;
        });
    }

    public java.util.List<JobTypeResponse> getAllJobTypes() {
        String sql = "SELECT P_JOB_TYPE_ID, CODE, DESCRIPTION, UPDATE_DATE, UPDATE_BY " +
                    "FROM P_JOB_TYPE ORDER BY P_JOB_TYPE_ID";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> 
            JobTypeResponse.builder()
                .pJobTypeId(rs.getLong("P_JOB_TYPE_ID"))
                .code(rs.getString("CODE"))
                .description(rs.getString("DESCRIPTION"))
                .updateDate(rs.getTimestamp("UPDATE_DATE").toLocalDateTime())
                .updateBy(rs.getString("UPDATE_BY"))
                .build());
    }

    public JobTypeResponse getJobTypeByCode(String code) {
        String sql = "SELECT P_JOB_TYPE_ID, CODE, DESCRIPTION, UPDATE_DATE, UPDATE_BY " +
                    "FROM P_JOB_TYPE WHERE CODE = ?";
        
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> 
                JobTypeResponse.builder()
                    .pJobTypeId(rs.getLong("P_JOB_TYPE_ID"))
                    .code(rs.getString("CODE"))
                    .description(rs.getString("DESCRIPTION"))
                    .updateDate(rs.getTimestamp("UPDATE_DATE").toLocalDateTime())
                    .updateBy(rs.getString("UPDATE_BY"))
                    .build(), code);
        } catch (Exception e) {
            return null; // No data found
        }
    }

}
