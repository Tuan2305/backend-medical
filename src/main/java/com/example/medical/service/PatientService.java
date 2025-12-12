package com.example.medical.service;

import com.example.medical.entity.Patient;
import com.example.medical.entity.SurveyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import com.example.medical.dto.response.PatientSurveySummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    Patient getPatientByCode(String patientCode);
    List<Patient> getRecentPatients(int hours);
    Page<Patient> getRecentPatients(int hours, Pageable pageable);

    List<Patient> getAllActivePatients();
    Page<Patient> getAllActivePatients(Pageable pageable);

    List<SurveyResponse> getPatientSurveyHistory(String patientCode);
    Page<SurveyResponse> getPatientSurveyHistory(String patientCode, Pageable pageable);

    boolean validatePatientCode(String patientCode);
    Patient createOrUpdatePatient(Patient patient);

    List<Patient> searchPatientsByName(String name);
    Page<Patient> searchPatientsByName(String name, Pageable pageable);

    void deactivatePatient(String patientCode);
    void activatePatient(String patientCode);
    Page<PatientSurveySummaryDTO> getPatientSurveySummaries(Pageable pageable);
    List<PatientSurveySummaryDTO> getAllPatientSurveySummaries();
}