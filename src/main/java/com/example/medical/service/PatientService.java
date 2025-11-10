package com.example.medical.service;

import com.example.medical.entity.Patient;
import com.example.medical.entity.SurveyResponse;
import java.util.List;

public interface PatientService {
    Patient getPatientByCode(String patientCode);
    List<Patient> getRecentPatients(int hours);
    List<Patient> getAllActivePatients();
    List<SurveyResponse> getPatientSurveyHistory(String patientCode);
    boolean validatePatientCode(String patientCode);
    Patient createOrUpdatePatient(Patient patient);
    List<Patient> searchPatientsByName(String name);
    void deactivatePatient(String patientCode);
    void activatePatient(String patientCode);
}