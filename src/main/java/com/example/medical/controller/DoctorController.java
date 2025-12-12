package com.example.medical.controller;

import com.example.medical.dto.response.*;
import com.example.medical.entity.Patient;
import com.example.medical.entity.SurveyResponse;
import com.example.medical.entity.enu.SurveyType;
import com.example.medical.mapper.PatientMapper;
import com.example.medical.mapper.SurveyResponseMapper;
import com.example.medical.service.DashboardService;
import com.example.medical.service.PatientService;
import com.example.medical.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
public class DoctorController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private SurveyResponseMapper surveyResponseMapper;

    @GetMapping("/patients/recent")
    public ResponseEntity<ApiResponse<PagedResponse<PatientDTO>>> getRecentPatients(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<Patient> patientsPage = patientService.getRecentPatients(hours, pageable);
            List<PatientDTO> patientDTOs = patientMapper.toDTOList(patientsPage.getContent());

            PagedResponse<PatientDTO> pagedResponse = PagedResponse.of(
                    patientDTOs, page, size, patientsPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách bệnh nhân gần đây thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/all")
    public ResponseEntity<ApiResponse<PagedResponse<PatientDTO>>> getAllActivePatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<Patient> patientsPage = patientService.getAllActivePatients(pageable);
            List<PatientDTO> patientDTOs = patientMapper.toDTOList(patientsPage.getContent());

            PagedResponse<PatientDTO> pagedResponse = PagedResponse.of(
                    patientDTOs, page, size, patientsPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách tất cả bệnh nhân thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/{patientCode}")
    public ResponseEntity<ApiResponse<PatientDTO>> getPatientByCode(@PathVariable String patientCode) {
        try {
            Patient patient = patientService.getPatientByCode(patientCode);
            PatientDTO patientDTO = patientMapper.toDTO(patient);

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy thông tin bệnh nhân thành công", patientDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không tìm thấy bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/{patientCode}/surveys")
    public ResponseEntity<ApiResponse<PagedResponse<SurveyResponseDTO>>> getPatientSurveyHistory(
            @PathVariable String patientCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<SurveyResponse> surveysPage = patientService.getPatientSurveyHistory(patientCode, pageable);
            List<SurveyResponseDTO> surveyDTOs = surveyResponseMapper.toDTOList(surveysPage.getContent());

            PagedResponse<SurveyResponseDTO> pagedResponse = PagedResponse.of(
                    surveyDTOs, page, size, surveysPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy lịch sử khảo sát của bệnh nhân thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy lịch sử khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/recent")
    public ResponseEntity<ApiResponse<PagedResponse<SurveyResponseDTO>>> getRecentSurveys(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<SurveyResponse> surveysPage = surveyService.getRecentSurveys(hours, pageable);
            List<SurveyResponseDTO> surveyDTOs = surveyResponseMapper.toDTOList(surveysPage.getContent());

            PagedResponse<SurveyResponseDTO> pagedResponse = PagedResponse.of(
                    surveyDTOs, page, size, surveysPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách khảo sát gần đây thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/all")
    public ResponseEntity<ApiResponse<PagedResponse<SurveyResponseDTO>>> getAllSurveys(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<SurveyResponse> surveysPage = surveyService.getAllSurveyResponses(pageable);
            List<SurveyResponseDTO> surveyDTOs = surveyResponseMapper.toDTOList(surveysPage.getContent());

            PagedResponse<SurveyResponseDTO> pagedResponse = PagedResponse.of(
                    surveyDTOs, page, size, surveysPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách tất cả khảo sát thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/type/{surveyType}")
    public ResponseEntity<ApiResponse<PagedResponse<SurveyResponseDTO>>> getSurveysByType(
            @PathVariable SurveyType surveyType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<SurveyResponse> surveysPage = surveyService.getSurveyResponsesBySurveyType(surveyType, pageable);
            List<SurveyResponseDTO> surveyDTOs = surveyResponseMapper.toDTOList(surveysPage.getContent());

            PagedResponse<SurveyResponseDTO> pagedResponse = PagedResponse.of(
                    surveyDTOs, page, size, surveysPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách khảo sát theo loại thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/search")
    public ResponseEntity<ApiResponse<PagedResponse<PatientDTO>>> searchPatients(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<Patient> patientsPage = patientService.searchPatientsByName(name, pageable);
            List<PatientDTO> patientDTOs = patientMapper.toDTOList(patientsPage.getContent());

            PagedResponse<PatientDTO> pagedResponse = PagedResponse.of(
                    patientDTOs, page, size, patientsPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Tìm kiếm bệnh nhân thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi tìm kiếm bệnh nhân: " + e.getMessage()));
        }
    }

    // Giữ lại API không phân trang cho dashboard/stats
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
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {
        try {
            DashboardStatsDTO stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy thống kê dashboard thành công", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy thống kê: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/summary")
    public ResponseEntity<ApiResponse<PagedResponse<PatientSurveySummaryDTO>>> getPatientSurveySummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "lastSurveyDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<PatientSurveySummaryDTO> summariesPage = patientService.getPatientSurveySummaries(pageable);

            PagedResponse<PatientSurveySummaryDTO> pagedResponse = PagedResponse.of(
                    summariesPage.getContent(), page, size, summariesPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy tổng quan bệnh nhân thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy tổng quan bệnh nhân: " + e.getMessage()));
        }
    }

    // Export all summaries (for Excel/CSV export)
    @GetMapping("/patients/summary/export")
    public ResponseEntity<ApiResponse<List<PatientSurveySummaryDTO>>> exportPatientSurveySummaries() {
        try {
            List<PatientSurveySummaryDTO> summaries = patientService.getAllPatientSurveySummaries();

            return ResponseEntity.ok(ApiResponse.success(
                    "Xuất dữ liệu tổng quan bệnh nhân thành công", summaries));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi xuất dữ liệu: " + e.getMessage()));
        }
    }

}