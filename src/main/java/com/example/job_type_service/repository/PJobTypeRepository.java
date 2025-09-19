package com.example.job_type_service.repository;

import com.example.job_type_service.entity.PJobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PJobTypeRepository extends JpaRepository<PJobType, Long> {
    

    Optional<PJobType> findByCode(String code);
    
    @Query("SELECT COUNT(j) > 0 FROM PJobType j WHERE j.code = :code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT j FROM PJobType j WHERE LOWER(j.description) LIKE LOWER('%' || :description || '%')")
    List<PJobType> findByDescriptionContainingIgnoreCase(@Param("description") String description);

    List<PJobType> findByUpdateBy(String updateBy);
    List<PJobType> findByUpdateDateAfter(java.time.LocalDateTime updateDate);
    List<PJobType> findByUpdateDateBefore(java.time.LocalDateTime updateDate);
    List<PJobType> findByUpdateDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    @Query("SELECT j FROM PJobType j ORDER BY j.updateDate DESC")
    List<PJobType> findAllOrderByUpdateDateDesc();

    @Query("SELECT j FROM PJobType j ORDER BY j.code ASC")
    List<PJobType> findAllOrderByCodeAsc();

    long countByUpdateBy(String updateBy);
    void deleteByCode(String code);
}
