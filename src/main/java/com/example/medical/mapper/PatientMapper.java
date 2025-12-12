package com.example.medical.mapper;

import com.example.medical.dto.response.PatientDTO;
import com.example.medical.entity.Patient;
import com.example.medical.entity.SurveyResponse;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientMapper {

    public PatientDTO toDTO(Patient patient) {
        if (patient == null) {
            return null;
        }

        LocalDateTime lastSurveyDate = null;
        int surveyCount = 0;

        if (patient.getSurveyResponses() != null && !patient.getSurveyResponses().isEmpty()) {
            surveyCount = patient.getSurveyResponses().size();
            lastSurveyDate = patient.getSurveyResponses().stream()
                    .map(SurveyResponse::getCreatedAt)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
        }

        return new PatientDTO(
                patient.getId(),
                patient.getPatientCode(),
                patient.getFullName(),
                patient.getBirthYear(),
                patient.getGender(),
                patient.getAddress(),
                patient.getOccupation(),
                patient.getCreatedAt(),
                patient.getUpdatedAt(),
                patient.getIsActive(),
                surveyCount,
                lastSurveyDate
        );
    }

    public List<PatientDTO> toDTOList(List<Patient> patients) {
        if (patients == null) {
            return List.of();
        }
        return patients.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Patient toEntity(PatientDTO dto) {
        if (dto == null) {
            return null;
        }

        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setPatientCode(dto.getPatientCode());
        patient.setFullName(dto.getFullName());
        patient.setBirthYear(dto.getBirthYear());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setOccupation(dto.getOccupation());
        patient.setCreatedAt(dto.getCreatedAt());
        patient.setUpdatedAt(dto.getUpdatedAt());
        patient.setIsActive(dto.getIsActive());

        return patient;
    }
}