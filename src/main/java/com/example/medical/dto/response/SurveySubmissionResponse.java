package com.example.medical.dto.response;

import com.example.medical.entity.enu.SurveyType;
import java.time.LocalDateTime;

public class SurveySubmissionResponse {
    private Long responseId;
    private Long patientId;
    private String patientName;
    private SurveyType surveyType;
    private Integer totalScore;
    private String interpretation;
    private LocalDateTime submittedAt;

    public SurveySubmissionResponse() {}

    public SurveySubmissionResponse(Long responseId, Long patientId, String patientName,
                                    SurveyType surveyType, Integer totalScore,
                                    String interpretation, LocalDateTime submittedAt) {
        this.responseId = responseId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.surveyType = surveyType;
        this.totalScore = totalScore;
        this.interpretation = interpretation;
        this.submittedAt = submittedAt;
    }

    // Getters and Setters
    public Long getResponseId() { return responseId; }
    public void setResponseId(Long responseId) { this.responseId = responseId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public SurveyType getSurveyType() { return surveyType; }
    public void setSurveyType(SurveyType surveyType) { this.surveyType = surveyType; }

    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }

    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}