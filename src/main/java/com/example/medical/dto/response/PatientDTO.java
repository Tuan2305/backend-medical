package com.example.medical.dto.response;

import com.example.medical.entity.enu.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String patientCode;
    private String fullName;
    private Integer birthYear;
    private Gender gender;
    private String address;
    private String occupation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private int surveyCount;
    private LocalDateTime lastSurveyDate;
}