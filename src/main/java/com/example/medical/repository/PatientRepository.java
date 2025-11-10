package com.example.medical.repository;

import com.example.medical.entity.Patient;
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

    List<Patient> findByFullNameContainingIgnoreCase(String fullName);

    @Query("SELECT p FROM Patient p WHERE p.isActive = true ORDER BY p.updatedAt DESC")
    List<Patient> findActivePatients();

    @Query("SELECT p FROM Patient p WHERE p.createdAt >= :startTime ORDER BY p.createdAt DESC")
    List<Patient> findRecentPatients(@Param("startTime") LocalDateTime startTime);

    @Query("SELECT p FROM Patient p JOIN p.surveyResponses sr WHERE sr.createdAt >= :startTime ORDER BY sr.createdAt DESC")
    List<Patient> findPatientsWithRecentSurveys(@Param("startTime") LocalDateTime startTime);
}