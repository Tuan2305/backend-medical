package com.example.medical.dto.request;

import com.example.medical.entity.enu.SurveyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseMappingDTO {
    private Long id;
    private String patientCode;
    private String patientName;
    private SurveyType surveyType;
    private Integer totalScore;
    private String interpretation;
    private LocalDateTime createdAt;
    private String responses;
}