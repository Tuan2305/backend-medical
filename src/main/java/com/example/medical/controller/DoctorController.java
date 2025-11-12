// filepath: c:\TUAN\code\Docker\medical fullstack\medical-backend\src\main\java\com\example\medical\controller\DoctorController.java
package com.example.medical.controller;

import com.example.medical.dto.response.ApiResponse;
import com.example.medical.dto.response.PatientDTO;
import com.example.medical.entity.Patient;
import com.example.medical.entity.SurveyResponse;
import com.example.medical.entity.enu.SurveyType;
import com.example.medical.service.PatientService;
import com.example.medical.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
public class DoctorController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private SurveyService surveyService;

    @GetMapping("/patients/recent")
    public ResponseEntity<ApiResponse<List<PatientDTO>>> getRecentPatients(
            @RequestParam(defaultValue = "24") int hours) {
        try {
            List<Patient> patients = patientService.getRecentPatients(hours);
            List<PatientDTO> patientDTOs = convertToDTOList(patients);

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách bệnh nhân gần đây thành công", patientDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/all")
    public ResponseEntity<ApiResponse<List<PatientDTO>>> getAllActivePatients() {
        try {
            List<Patient> patients = patientService.getAllActivePatients();
            List<PatientDTO> patientDTOs = convertToDTOList(patients);

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách tất cả bệnh nhân thành công", patientDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/{patientCode}")
    public ResponseEntity<ApiResponse<PatientDTO>> getPatientByCode(@PathVariable String patientCode) {
        try {
            Patient patient = patientService.getPatientByCode(patientCode);
            PatientDTO patientDTO = convertToDTO(patient);

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy thông tin bệnh nhân thành công", patientDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không tìm thấy bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/{patientCode}/surveys")
    public ResponseEntity<ApiResponse<List<SurveyResponse>>> getPatientSurveyHistory(
            @PathVariable String patientCode) {
        try {
            List<SurveyResponse> surveys = patientService.getPatientSurveyHistory(patientCode);

            // Remove patient object from each survey to avoid circular reference
            surveys.forEach(survey -> survey.setPatient(null));

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy lịch sử khảo sát của bệnh nhân thành công", surveys));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy lịch sử khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/recent")
    public ResponseEntity<ApiResponse<List<SurveyResponseDTO>>> getRecentSurveys(
            @RequestParam(defaultValue = "24") int hours) {
        try {
            List<SurveyResponse> surveys = surveyService.getRecentSurveys(hours);
            List<SurveyResponseDTO> surveyDTOs = surveys.stream()
                    .map(this::convertSurveyToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách khảo sát gần đây thành công", surveyDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/type/{surveyType}")
    public ResponseEntity<ApiResponse<List<SurveyResponseDTO>>> getSurveysByType(
            @PathVariable SurveyType surveyType) {
        try {
            List<SurveyResponse> surveys = surveyService.getSurveyResponsesBySurveyType(surveyType);
            List<SurveyResponseDTO> surveyDTOs = surveys.stream()
                    .map(this::convertSurveyToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách khảo sát theo loại thành công", surveyDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/search")
    public ResponseEntity<ApiResponse<List<PatientDTO>>> searchPatients(
            @RequestParam String name) {
        try {
            List<Patient> patients = patientService.searchPatientsByName(name);
            List<PatientDTO> patientDTOs = convertToDTOList(patients);

            return ResponseEntity.ok(ApiResponse.success(
                    "Tìm kiếm bệnh nhân thành công", patientDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi tìm kiếm bệnh nhân: " + e.getMessage()));
        }
    }

    @PostMapping("/patients/{patientCode}/validate")
    public ResponseEntity<ApiResponse<Boolean>> validatePatientCode(@PathVariable String patientCode) {
        try {
            boolean isValid = patientService.validatePatientCode(patientCode);
            return ResponseEntity.ok(ApiResponse.success(
                    isValid ? "Mã bệnh nhân hợp lệ" : "Mã bệnh nhân không hợp lệ", isValid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi xác thực mã bệnh nhân: " + e.getMessage()));
        }
    }

    @PostMapping("/patients/{patientCode}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivatePatient(@PathVariable String patientCode) {
        try {
            patientService.deactivatePatient(patientCode);
            return ResponseEntity.ok(ApiResponse.success(
                    "Đã vô hiệu hóa bệnh nhân thành công", "Deactivated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi vô hiệu hóa bệnh nhân: " + e.getMessage()));
        }
    }

    @PostMapping("/patients/{patientCode}/activate")
    public ResponseEntity<ApiResponse<String>> activatePatient(@PathVariable String patientCode) {
        try {
            patientService.activatePatient(patientCode);
            return ResponseEntity.ok(ApiResponse.success(
                    "Đã kích hoạt bệnh nhân thành công", "Activated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi kích hoạt bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        try {
            DashboardStats stats = new DashboardStats();
            stats.setTotalPatients(patientService.getAllActivePatients().size());
            stats.setRecentSurveys24h(surveyService.getRecentSurveys(24).size());
            stats.setRecentSurveys7d(surveyService.getRecentSurveys(24 * 7).size());
            stats.setRecentPatients24h(patientService.getRecentPatients(24).size());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy thống kê dashboard thành công", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy thống kê: " + e.getMessage()));
        }
    }

    // Helper methods
    private List<PatientDTO> convertToDTOList(List<Patient> patients) {
        return patients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PatientDTO convertToDTO(Patient patient) {
        LocalDateTime lastSurveyDate = null;
        int surveyCount = 0;

        if (patient.getSurveyResponses() != null && !patient.getSurveyResponses().isEmpty()) {
            surveyCount = patient.getSurveyResponses().size();
            lastSurveyDate = patient.getSurveyResponses().stream()
                    .map(SurveyResponse::getCreatedAt)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
        }

        return new PatientDTO(
                patient.getId(),
                patient.getPatientCode(),
                patient.getFullName(),
                patient.getBirthYear(),
                patient.getGender(),
                patient.getAddress(),
                patient.getOccupation(),
                patient.getCreatedAt(),
                patient.getUpdatedAt(),
                patient.getIsActive(),
                surveyCount,
                lastSurveyDate
        );
    }

    private SurveyResponseDTO convertSurveyToDTO(SurveyResponse survey) {
        return new SurveyResponseDTO(
                survey.getId(),
                survey.getPatient() != null ? survey.getPatient().getPatientCode() : null,
                survey.getPatient() != null ? survey.getPatient().getFullName() : null,
                survey.getSurveyType(),
                survey.getTotalScore(),
                survey.getInterpretation(),
                survey.getCreatedAt(),
                survey.getResponses()
        );
    }

    // DTOs as inner classes
    public static class DashboardStats {
        private int totalPatients;
        private int recentSurveys24h;
        private int recentSurveys7d;
        private int recentPatients24h;

        // Getters and Setters
        public int getTotalPatients() { return totalPatients; }
        public void setTotalPatients(int totalPatients) { this.totalPatients = totalPatients; }

        public int getRecentSurveys24h() { return recentSurveys24h; }
        public void setRecentSurveys24h(int recentSurveys24h) { this.recentSurveys24h = recentSurveys24h; }

        public int getRecentSurveys7d() { return recentSurveys7d; }
        public void setRecentSurveys7d(int recentSurveys7d) { this.recentSurveys7d = recentSurveys7d; }

        public int getRecentPatients24h() { return recentPatients24h; }
        public void setRecentPatients24h(int recentPatients24h) { this.recentPatients24h = recentPatients24h; }
    }

    public static class SurveyResponseDTO {
        private Long id;
        private String patientCode;
        private String patientName;
        private SurveyType surveyType;
        private Integer totalScore;
        private String interpretation;
        private LocalDateTime createdAt;
        private String responses;

        public SurveyResponseDTO(Long id, String patientCode, String patientName,
                                 SurveyType surveyType, Integer totalScore, String interpretation,
                                 LocalDateTime createdAt, String responses) {
            this.id = id;
            this.patientCode = patientCode;
            this.patientName = patientName;
            this.surveyType = surveyType;
            this.totalScore = totalScore;
            this.interpretation = interpretation;
            this.createdAt = createdAt;
            this.responses = responses;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getPatientCode() { return patientCode; }
        public void setPatientCode(String patientCode) { this.patientCode = patientCode; }

        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }

        public SurveyType getSurveyType() { return surveyType; }
        public void setSurveyType(SurveyType surveyType) { this.surveyType = surveyType; }

        public Integer getTotalScore() { return totalScore; }
        public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }

        public String getInterpretation() { return interpretation; }
        public void setInterpretation(String interpretation) { this.interpretation = interpretation; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public String getResponses() { return responses; }
        public void setResponses(String responses) { this.responses = responses; }
    }
}