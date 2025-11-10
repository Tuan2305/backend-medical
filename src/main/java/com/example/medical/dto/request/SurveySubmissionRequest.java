package com.example.medical.dto.request;

import com.example.medical.entity.enu.Gender;
import com.example.medical.entity.enu.SurveyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.Map;

public class SurveySubmissionRequest {
    // Patient identification
    @NotBlank(message = "Mã bệnh nhân là bắt buộc")
    private String patientCode;

    @NotBlank(message = "Họ và tên là bắt buộc")
    private String fullName;

    @NotNull(message = "Năm sinh là bắt buộc")
    @Min(value = 1900, message = "Năm sinh phải từ 1900 trở lên")
    @Max(value = 2024, message = "Năm sinh không được vượt quá năm hiện tại")
    private Integer birthYear;

    @NotNull(message = "Giới tính là bắt buộc")
    private Gender gender;

    private String address;
    private String occupation;

    // Survey information
    @NotNull(message = "Loại khảo sát là bắt buộc")
    private SurveyType surveyType;

    @NotNull(message = "Câu trả lời khảo sát là bắt buộc")
    private Map<String, Object> responses;

    public SurveySubmissionRequest() {}

    // Getters and Setters
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

    public SurveyType getSurveyType() { return surveyType; }
    public void setSurveyType(SurveyType surveyType) { this.surveyType = surveyType; }

    public Map<String, Object> getResponses() { return responses; }
    public void setResponses(Map<String, Object> responses) { this.responses = responses; }
}