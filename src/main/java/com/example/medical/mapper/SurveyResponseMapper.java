package com.example.medical.mapper;

import com.example.medical.dto.response.SurveyResponseDTO;
import com.example.medical.entity.SurveyResponse;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SurveyResponseMapper {

    public SurveyResponseDTO toDTO(SurveyResponse survey) {
        if (survey == null) {
            return null;
        }

        return new SurveyResponseDTO(
                survey.getId(),
                survey.getPatient() != null ? survey.getPatient().getPatientCode() : null,
                survey.getPatient() != null ? survey.getPatient().getFullName() : null,
                survey.getSurveyType(),
                survey.getTotalScore(),
                survey.getInterpretation(),
                survey.getCreatedAt(),
                survey.getResponses()
        );
    }

    public List<SurveyResponseDTO> toDTOList(List<SurveyResponse> surveys) {
        if (surveys == null) {
            return List.of();
        }
        return surveys.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SurveyResponse toEntity(SurveyResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        SurveyResponse survey = new SurveyResponse();
        survey.setId(dto.getId());
        survey.setSurveyType(dto.getSurveyType());
        survey.setTotalScore(dto.getTotalScore());
        survey.setInterpretation(dto.getInterpretation());
        survey.setCreatedAt(dto.getCreatedAt());
        survey.setResponses(dto.getResponses());

        return survey;
    }
}