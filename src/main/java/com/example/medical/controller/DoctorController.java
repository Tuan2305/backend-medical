package com.example.medical.controller;

import com.example.medical.dto.response.ApiResponse;
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

@RestController
@RequestMapping("/api/doctor")

@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
public class DoctorController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private SurveyService surveyService;

    @GetMapping("/patients/recent")
    public ResponseEntity<ApiResponse<List<Patient>>> getRecentPatients(
            @RequestParam(defaultValue = "24") int hours) {
        try {
            List<Patient> patients = patientService.getRecentPatients(hours);
            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách bệnh nhân gần đây thành công", patients));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/all")
    public ResponseEntity<ApiResponse<List<Patient>>> getAllActivePatients() {
        try {
            List<Patient> patients = patientService.getAllActivePatients();
            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách tất cả bệnh nhân thành công", patients));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/{patientCode}")
    public ResponseEntity<ApiResponse<Patient>> getPatientByCode(@PathVariable String patientCode) {
        try {
            Patient patient = patientService.getPatientByCode(patientCode);
            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy thông tin bệnh nhân thành công", patient));
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
            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy lịch sử khảo sát của bệnh nhân thành công", surveys));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy lịch sử khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/recent")
    public ResponseEntity<ApiResponse<List<SurveyResponse>>> getRecentSurveys(
            @RequestParam(defaultValue = "24") int hours) {
        try {
            List<SurveyResponse> surveys = surveyService.getRecentSurveys(hours);
            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách khảo sát gần đây thành công", surveys));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/type/{surveyType}")
    public ResponseEntity<ApiResponse<List<SurveyResponse>>> getSurveysByType(
            @PathVariable SurveyType surveyType) {
        try {
            List<SurveyResponse> surveys = surveyService.getSurveyResponsesBySurveyType(surveyType);
            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách khảo sát theo loại thành công", surveys));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/search")
    public ResponseEntity<ApiResponse<List<Patient>>> searchPatients(
            @RequestParam String name) {
        try {
            List<Patient> patients = patientService.searchPatientsByName(name);
            return ResponseEntity.ok(ApiResponse.success(
                    "Tìm kiếm bệnh nhân thành công", patients));
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

    // Inner class for dashboard statistics
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
}