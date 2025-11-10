package com.example.medical.service;

import com.example.medical.dto.request.SurveySubmissionRequest;
import com.example.medical.entity.SurveyResponse;
import com.example.medical.entity.enu.SurveyType;
import java.time.LocalDateTime;
import java.util.List;

public interface SurveyService {
    SurveyResponse submitSurvey(SurveySubmissionRequest request);
    List<SurveyResponse> getAllSurveyResponses();
    List<SurveyResponse> getSurveyResponsesByPatient(Long patientId);
    List<SurveyResponse> getSurveyResponsesBySurveyType(SurveyType surveyType);
    List<SurveyResponse> getRecentSurveys(int hours);
}