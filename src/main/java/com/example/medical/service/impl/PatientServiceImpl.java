package com.example.medical.service.impl;

import com.example.medical.entity.Patient;
import com.example.medical.entity.SurveyResponse;
import com.example.medical.repository.PatientRepository;
import com.example.medical.repository.SurveyResponseRepository;
import com.example.medical.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    @Override
    public Patient getPatientByCode(String patientCode) {
        return patientRepository.findByPatientCode(patientCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với mã: " + patientCode));
    }

    @Override
    public List<Patient> getRecentPatients(int hours) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        return patientRepository.findPatientsWithRecentSurveys(startTime);
    }

    @Override
    public List<Patient> getAllActivePatients() {
        return patientRepository.findActivePatients();
    }

    @Override
    public List<SurveyResponse> getPatientSurveyHistory(String patientCode) {
        // Validate patient exists
        Patient patient = getPatientByCode(patientCode);
        return surveyResponseRepository.findByPatientCodeOrderByCreatedAtDesc(patientCode);
    }

    @Override
    public boolean validatePatientCode(String patientCode) {
        if (patientCode == null || patientCode.trim().isEmpty()) {
            return false;
        }

        // Format: BN + 6 chữ số (ví dụ: BN123456)
        return patientCode.matches("^BN\\d{6}$") && patientCode.length() == 8;
    }

    @Override
    public Patient createOrUpdatePatient(Patient patient) {
        if (patient.getId() == null) {
            // Creating new patient
            patient.setCreatedAt(LocalDateTime.now());
            patient.setIsActive(true);
        }
        patient.setUpdatedAt(LocalDateTime.now());
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> searchPatientsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        return patientRepository.findByFullNameContainingIgnoreCase(name.trim());
    }

    @Override
    public void deactivatePatient(String patientCode) {
        Patient patient = getPatientByCode(patientCode);
        patient.setIsActive(false);
        patient.setUpdatedAt(LocalDateTime.now());
        patientRepository.save(patient);
    }

    @Override
    public void activatePatient(String patientCode) {
        Patient patient = getPatientByCode(patientCode);
        patient.setIsActive(true);
        patient.setUpdatedAt(LocalDateTime.now());
        patientRepository.save(patient);
    }

    public boolean existsByPatientCode(String patientCode) {
        return patientRepository.existsByPatientCode(patientCode);
    }

    public List<Patient> getPatientsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return patientRepository.findRecentPatients(startDate);
    }
}