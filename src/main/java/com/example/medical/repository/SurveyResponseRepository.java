package com.example.medical.repository;

import com.example.medical.entity.SurveyResponse;
import com.example.medical.entity.enu.SurveyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findByPatientId(Long patientId);
    Page<SurveyResponse> findByPatientId(Long patientId, Pageable pageable);

    List<SurveyResponse> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    Page<SurveyResponse> findByPatientIdOrderByCreatedAtDesc(Long patientId, Pageable pageable);

    List<SurveyResponse> findBySurveyType(SurveyType surveyType);
    Page<SurveyResponse> findBySurveyType(SurveyType surveyType, Pageable pageable);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.createdAt BETWEEN :startDate AND :endDate ORDER BY sr.createdAt DESC")
    List<SurveyResponse> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.createdAt BETWEEN :startDate AND :endDate ORDER BY sr.createdAt DESC")
    Page<SurveyResponse> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate,
                                         Pageable pageable);

    Page<SurveyResponse> findByDoctorId(Long doctorId, Pageable pageable);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.patient.fullName LIKE %:patientName%")
    List<SurveyResponse> findByPatientNameContaining(@Param("patientName") String patientName);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.patient.fullName LIKE %:patientName% ORDER BY sr.createdAt DESC")
    Page<SurveyResponse> findByPatientNameContaining(@Param("patientName") String patientName, Pageable pageable);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.patient.patientCode = :patientCode ORDER BY sr.createdAt DESC")
    List<SurveyResponse> findByPatientCodeOrderByCreatedAtDesc(@Param("patientCode") String patientCode);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.createdAt >= :sinceTime ORDER BY sr.createdAt DESC")
    List<SurveyResponse> findRecentResponses(@Param("sinceTime") LocalDateTime sinceTime);

    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.createdAt >= :sinceTime ORDER BY sr.createdAt DESC")
    Page<SurveyResponse> findRecentResponses(@Param("sinceTime") LocalDateTime sinceTime, Pageable pageable);

    @Query("SELECT COUNT(sr) FROM SurveyResponse sr WHERE sr.createdAt >= :sinceTime")
    long countRecentSurveys(@Param("sinceTime") LocalDateTime sinceTime);

    @Query("SELECT COUNT(sr) FROM SurveyResponse sr")
    long countAllSurveys();

    Optional<SurveyResponse> findTopByPatientIdAndSurveyTypeOrderByCreatedAtDesc(Long patientId, SurveyType surveyType);

    // Count surveys by patient
    long countByPatientId(Long patientId);

}