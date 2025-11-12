package com.example.medical.dto.response;

import com.example.medical.entity.enu.Gender;
import java.time.LocalDateTime;

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

    public PatientDTO() {}

    public PatientDTO(Long id, String patientCode, String fullName, Integer birthYear,
                      Gender gender, String address, String occupation,
                      LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isActive,
                      int surveyCount, LocalDateTime lastSurveyDate) {
        this.id = id;
        this.patientCode = patientCode;
        this.fullName = fullName;
        this.birthYear = birthYear;
        this.gender = gender;
        this.address = address;
        this.occupation = occupation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.surveyCount = surveyCount;
        this.lastSurveyDate = lastSurveyDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientCode() { return patientCode; }
    public void setPatientCode(String patientCode) { this.patientCode = patientCode; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public int getSurveyCount() { return surveyCount; }
    public void setSurveyCount(int surveyCount) { this.surveyCount = surveyCount; }

    public LocalDateTime getLastSurveyDate() { return lastSurveyDate; }
    public void setLastSurveyDate(LocalDateTime lastSurveyDate) { this.lastSurveyDate = lastSurveyDate; }
}