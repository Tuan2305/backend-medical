package com.example.medical.service.impl;

import com.example.medical.dto.response.DashboardStatsDTO;
import com.example.medical.repository.PatientRepository;
import com.example.medical.repository.SurveyResponseRepository;
import com.example.medical.service.DashboardService;
import com.example.medical.service.PatientService;
import com.example.medical.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        try {
            // Sử dụng COUNT queries thay vì lấy toàn bộ data
            int totalPatients = (int) patientRepository.countActivePatients();

            LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusHours(24 * 7);

            int recentSurveys24h = (int) surveyResponseRepository.countRecentSurveys(twentyFourHoursAgo);
            int recentSurveys7d = (int) surveyResponseRepository.countRecentSurveys(sevenDaysAgo);
            int recentPatients24h = (int) patientRepository.countRecentPatients(twentyFourHoursAgo);

            return new DashboardStatsDTO(
                    totalPatients,
                    recentSurveys24h,
                    recentSurveys7d,
                    recentPatients24h
            );
        } catch (Exception e) {
            System.err.println("ERROR in getDashboardStats: " + e.getMessage());
            // Fallback với dữ liệu mặc định
            return new DashboardStatsDTO(0, 0, 0, 0);
        }
    }
}