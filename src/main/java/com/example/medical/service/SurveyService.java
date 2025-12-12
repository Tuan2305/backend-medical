package com.example.medical.service;

import com.example.medical.dto.request.SurveySubmissionRequest;
import com.example.medical.entity.SurveyResponse;
import com.example.medical.entity.enu.SurveyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface SurveyService {
    SurveyResponse submitSurvey(SurveySubmissionRequest request);

    List<SurveyResponse> getAllSurveyResponses();
    Page<SurveyResponse> getAllSurveyResponses(Pageable pageable);

    List<SurveyResponse> getSurveyResponsesByPatient(Long patientId);
    Page<SurveyResponse> getSurveyResponsesByPatient(Long patientId, Pageable pageable);

    List<SurveyResponse> getSurveyResponsesBySurveyType(SurveyType surveyType);
    Page<SurveyResponse> getSurveyResponsesBySurveyType(SurveyType surveyType, Pageable pageable);

    List<SurveyResponse> getRecentSurveys(int hours);
    Page<SurveyResponse> getRecentSurveys(int hours, Pageable pageable);
}