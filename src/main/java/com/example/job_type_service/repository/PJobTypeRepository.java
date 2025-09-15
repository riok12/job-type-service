package com.example.job_type_service.repository;

import com.example.job_type_service.entity.PJobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PJobTypeRepository extends JpaRepository<PJobType, Long> {
    
    Optional<PJobType> findByCode(String code);
    
    boolean existsByCode(String code);
}
