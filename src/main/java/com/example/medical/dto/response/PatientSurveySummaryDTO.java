package com.example.medical.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientSurveySummaryDTO {
    private Long patientId;
    private String patientCode;
    private String fullName;
    private LocalDateTime lastSurveyDate;

    // Survey scores (null if not completed)
    private Integer psqiScore;
    private LocalDateTime psqiDate;

    private Integer beckScore;
    private LocalDateTime beckDate;

    private Integer zungScore;
    private LocalDateTime zungDate;

    private Integer dass21Score;
    private LocalDateTime dass21Date;

    private Integer mmseScore;
    private LocalDateTime mmseDate;

    // Total completed surveys
    private int totalSurveys;
}