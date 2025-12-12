package com.example.medical.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private int totalPatients;
    private int recentSurveys24h;
    private int recentSurveys7d;
    private int recentPatients24h;
}