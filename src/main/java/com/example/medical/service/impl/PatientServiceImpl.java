package com.example.medical.service.impl;

import com.example.medical.dto.response.PatientSurveySummaryDTO;
import com.example.medical.entity.Patient;
import com.example.medical.entity.SurveyResponse;
import com.example.medical.entity.enu.SurveyType;
import com.example.medical.repository.PatientRepository;
import com.example.medical.repository.SurveyResponseRepository;
import com.example.medical.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // Methods không có pagination cho dashboard
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
        Patient patient = getPatientByCode(patientCode);
        return surveyResponseRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId());
    }

    @Override
    public List<Patient> searchPatientsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        return patientRepository.findByFullNameContainingIgnoreCase(name.trim());
    }

    // Methods có pagination cho API endpoints
    @Override
    public Page<Patient> getRecentPatients(int hours, Pageable pageable) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        return patientRepository.findPatientsWithRecentSurveys(startTime, pageable);
    }

    @Override
    public Page<Patient> getAllActivePatients(Pageable pageable) {
        return patientRepository.findActivePatients(pageable);
    }

    @Override
    public Page<SurveyResponse> getPatientSurveyHistory(String patientCode, Pageable pageable) {
        Patient patient = getPatientByCode(patientCode);
        return surveyResponseRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId(), pageable);
    }

    @Override
    public Page<Patient> searchPatientsByName(String name, Pageable pageable) {
        if (name == null || name.trim().isEmpty()) {
            return Page.empty();
        }
        return patientRepository.findByFullNameContainingIgnoreCase(name.trim(), pageable);
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

    @Override
    public Page<PatientSurveySummaryDTO> getPatientSurveySummaries(Pageable pageable) {
        Page<Patient> patientsPage = patientRepository.findActivePatients(pageable);

        List<PatientSurveySummaryDTO> summaries = patientsPage.getContent().stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(summaries, pageable, patientsPage.getTotalElements());
    }

    @Override
    public List<PatientSurveySummaryDTO> getAllPatientSurveySummaries() {
        List<Patient> patients = patientRepository.findActivePatients();

        return patients.stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    private PatientSurveySummaryDTO convertToSummaryDTO(Patient patient) {
        PatientSurveySummaryDTO dto = new PatientSurveySummaryDTO();
        dto.setPatientId(patient.getId());
        dto.setPatientCode(patient.getPatientCode());
        dto.setFullName(patient.getFullName());

        // Get latest survey dates and scores for each type
        Optional<SurveyResponse> psqi = surveyResponseRepository
                .findTopByPatientIdAndSurveyTypeOrderByCreatedAtDesc(patient.getId(), SurveyType.PSQI);
        if (psqi.isPresent()) {
            dto.setPsqiScore(psqi.get().getTotalScore());
            dto.setPsqiDate(psqi.get().getCreatedAt());
        }

        Optional<SurveyResponse> beck = surveyResponseRepository
                .findTopByPatientIdAndSurveyTypeOrderByCreatedAtDesc(patient.getId(), SurveyType.BECK);
        if (beck.isPresent()) {
            dto.setBeckScore(beck.get().getTotalScore());
            dto.setBeckDate(beck.get().getCreatedAt());
        }

        Optional<SurveyResponse> zung = surveyResponseRepository
                .findTopByPatientIdAndSurveyTypeOrderByCreatedAtDesc(patient.getId(), SurveyType.ZUNG);
        if (zung.isPresent()) {
            dto.setZungScore(zung.get().getTotalScore());
            dto.setZungDate(zung.get().getCreatedAt());
        }

        Optional<SurveyResponse> dass21 = surveyResponseRepository
                .findTopByPatientIdAndSurveyTypeOrderByCreatedAtDesc(patient.getId(), SurveyType.DASS21);
        if (dass21.isPresent()) {
            dto.setDass21Score(dass21.get().getTotalScore());
            dto.setDass21Date(dass21.get().getCreatedAt());
        }

        Optional<SurveyResponse> mmse = surveyResponseRepository
                .findTopByPatientIdAndSurveyTypeOrderByCreatedAtDesc(patient.getId(), SurveyType.MMSE);
        if (mmse.isPresent()) {
            dto.setMmseScore(mmse.get().getTotalScore());
            dto.setMmseDate(mmse.get().getCreatedAt());
        }

        // Set last survey date (latest among all surveys)
        LocalDateTime lastSurveyDate = null;
        if (psqi.isPresent()) lastSurveyDate = psqi.get().getCreatedAt();
        if (beck.isPresent() && (lastSurveyDate == null || beck.get().getCreatedAt().isAfter(lastSurveyDate))) {
            lastSurveyDate = beck.get().getCreatedAt();
        }
        if (zung.isPresent() && (lastSurveyDate == null || zung.get().getCreatedAt().isAfter(lastSurveyDate))) {
            lastSurveyDate = zung.get().getCreatedAt();
        }
        if (dass21.isPresent() && (lastSurveyDate == null || dass21.get().getCreatedAt().isAfter(lastSurveyDate))) {
            lastSurveyDate = dass21.get().getCreatedAt();
        }
        if (mmse.isPresent() && (lastSurveyDate == null || mmse.get().getCreatedAt().isAfter(lastSurveyDate))) {
            lastSurveyDate = mmse.get().getCreatedAt();
        }
        dto.setLastSurveyDate(lastSurveyDate);

        // Count total surveys
        int totalSurveys = (int) surveyResponseRepository.countByPatientId(patient.getId());
        dto.setTotalSurveys(totalSurveys);

        return dto;
    }

    public boolean existsByPatientCode(String patientCode) {
        return patientRepository.existsByPatientCode(patientCode);
    }

    public List<Patient> getPatientsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return patientRepository.findRecentPatients(startDate);
    }
}