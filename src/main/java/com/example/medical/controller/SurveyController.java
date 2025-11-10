package com.example.medical.controller;

import com.example.medical.dto.request.SurveySubmissionRequest;
import com.example.medical.dto.response.ApiResponse;
import com.example.medical.dto.response.SurveySubmissionResponse;
import com.example.medical.entity.SurveyResponse;
import com.example.medical.entity.enu.SurveyType;
import com.example.medical.service.PatientService;
import com.example.medical.service.SurveyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private PatientService patientService;

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SurveySubmissionResponse>> submitSurvey(
            @Valid @RequestBody SurveySubmissionRequest request) {
        try {
            // Validate patient code first
            if (!patientService.validatePatientCode(request.getPatientCode())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Mã bệnh nhân không đúng định dạng. Mã phải có dạng BN + 6 chữ số (ví dụ: BN123456)"));
            }

            SurveyResponse response = surveyService.submitSurvey(request);

            SurveySubmissionResponse responseDto = new SurveySubmissionResponse(
                    response.getId(),
                    response.getPatient().getId(),
                    response.getPatient().getFullName(),
                    response.getSurveyType(),
                    response.getTotalScore(),
                    response.getInterpretation(),
                    response.getCreatedAt()
            );

            return ResponseEntity.ok(ApiResponse.success("Gửi khảo sát thành công", responseDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Lỗi khi gửi khảo sát: " + e.getMessage()));
        }
    }

    @PostMapping("/validate-patient")
    public ResponseEntity<ApiResponse<String>> validatePatient(
            @RequestParam String patientCode) {
        try {
            if (!patientService.validatePatientCode(patientCode)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Mã bệnh nhân không đúng định dạng"));
            }

            // Check if patient exists
            try {
                patientService.getPatientByCode(patientCode);
                return ResponseEntity.ok(ApiResponse.success("Mã bệnh nhân hợp lệ", "existing"));
            } catch (RuntimeException e) {
                return ResponseEntity.ok(ApiResponse.success("Mã bệnh nhân hợp lệ (mới)", "new"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Lỗi khi xác thực mã bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<SurveyType[]>> getSurveyTypes() {
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách loại khảo sát thành công", SurveyType.values()));
    }

    // Doctor endpoints moved to DoctorController for better security
}