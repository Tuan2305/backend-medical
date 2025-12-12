package com.example.medical.repository;

import com.example.medical.entity.Patient;
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
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPatientCode(String patientCode);

    boolean existsByPatientCode(String patientCode);

    // Thêm phân trang cho tìm kiếm theo tên
    Page<Patient> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    List<Patient> findByFullNameContainingIgnoreCase(String fullName);

    // Phân trang cho bệnh nhân active
    @Query("SELECT p FROM Patient p WHERE p.isActive = true ORDER BY p.updatedAt DESC")
    Page<Patient> findActivePatients(Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.isActive = true ORDER BY p.updatedAt DESC")
    List<Patient> findActivePatients();

    // Phân trang cho bệnh nhân gần đây
    @Query("SELECT p FROM Patient p WHERE p.createdAt >= :startTime ORDER BY p.createdAt DESC")
    Page<Patient> findRecentPatients(@Param("startTime") LocalDateTime startTime, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.createdAt >= :startTime ORDER BY p.createdAt DESC")
    List<Patient> findRecentPatients(@Param("startTime") LocalDateTime startTime);

    // Phân trang cho bệnh nhân có khảo sát gần đây
    @Query("SELECT DISTINCT p FROM Patient p JOIN p.surveyResponses sr WHERE sr.createdAt >= :startTime ORDER BY sr.createdAt DESC")
    Page<Patient> findPatientsWithRecentSurveys(@Param("startTime") LocalDateTime startTime, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Patient p JOIN p.surveyResponses sr WHERE sr.createdAt >= :startTime ORDER BY sr.createdAt DESC")
    List<Patient> findPatientsWithRecentSurveys(@Param("startTime") LocalDateTime startTime);

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.isActive = true")
    long countActivePatients();

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.createdAt >= :startTime")
    long countRecentPatients(@Param("startTime") LocalDateTime startTime);
}