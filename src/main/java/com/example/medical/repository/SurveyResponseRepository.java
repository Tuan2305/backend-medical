package com.example.medical.repository;

import com.example.medical.entity.SurveyResponse;
import com.example.medical.entity.enu.SurveyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findByPatientId(Long patientId);

    List<SurveyResponse> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    List<SurveyResponse> findBySurveyType(SurveyType surveyType);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.createdAt BETWEEN :startDate AND :endDate ORDER BY sr.createdAt DESC")
    List<SurveyResponse> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    Page<SurveyResponse> findByDoctorId(Long doctorId, Pageable pageable);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.patient.fullName LIKE %:patientName%")
    List<SurveyResponse> findByPatientNameContaining(@Param("patientName") String patientName);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.patient.patientCode = :patientCode ORDER BY sr.createdAt DESC")
    List<SurveyResponse> findByPatientCodeOrderByCreatedAtDesc(@Param("patientCode") String patientCode);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.createdAt >= :sinceTime ORDER BY sr.createdAt DESC")
    List<SurveyResponse> findRecentResponses(@Param("sinceTime") LocalDateTime sinceTime);
}