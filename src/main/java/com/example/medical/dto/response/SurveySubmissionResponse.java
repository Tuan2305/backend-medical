package com.example.medical.dto.response;

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
public class SurveySubmissionResponse {
    private Long responseId;
    private Long patientId;
    private String patientName;
    private SurveyType surveyType;
    private Integer totalScore;
    private String interpretation;
    private LocalDateTime submittedAt;
}