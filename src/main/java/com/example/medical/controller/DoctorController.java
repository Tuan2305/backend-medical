package com.example.medical.controller;

import com.example.medical.dto.response.*;
import com.example.medical.entity.BenhNhan;
import com.example.medical.entity.PhanHoiKhaoSat;
import com.example.medical.entity.enu.LoaiKhaoSat;
import com.example.medical.mapper.BenhNhanMapper;
import com.example.medical.mapper.PhanHoiKhaoSatMapper;
import com.example.medical.service.BangDieuKhienService;
import com.example.medical.service.BenhNhanService;
import com.example.medical.service.KhaoSatService;
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
    private BenhNhanService benhNhanService;

    @Autowired
    private KhaoSatService khaoSatService;

    @Autowired
    private BangDieuKhienService bangDieuKhienService;

    @Autowired
    private BenhNhanMapper benhNhanMapper;

    @Autowired
    private PhanHoiKhaoSatMapper phanHoiKhaoSatMapper;

    @GetMapping("/patients/recent")
    public ResponseEntity<ApiResponse<PagedResponse<benhNhanDTO>>> getRecentPatients(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            // Map English field names to Vietnamese entity attributes
            String mappedSortBy = switch (sortBy) {
                case "updatedAt" -> "ngayCapNhat";
                case "createdAt" -> "ngayTao";
                case "fullName" -> "hoTen";
                case "patientCode" -> "maBenhNhan";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<BenhNhan> patientsPage = benhNhanService.layBenhNhanGanDay(hours, pageable);
            List<benhNhanDTO> patientDTOs = benhNhanMapper.toDTOList(patientsPage.getContent());

            PagedResponse<benhNhanDTO> pagedResponse = PagedResponse.of(
                    patientDTOs, page, size, patientsPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách bệnh nhân gần đây thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/all")
    public ResponseEntity<ApiResponse<PagedResponse<benhNhanDTO>>> getAllActivePatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            // Map English field names to Vietnamese entity attributes
            String mappedSortBy = switch (sortBy) {
                case "updatedAt" -> "ngayCapNhat";
                case "createdAt" -> "ngayTao";
                case "fullName" -> "hoTen";
                case "patientCode" -> "maBenhNhan";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<BenhNhan> patientsPage = benhNhanService.layTatCaBenhNhanDangHoatDong(pageable);
            List<benhNhanDTO> patientDTOs = benhNhanMapper.toDTOList(patientsPage.getContent());

            PagedResponse<benhNhanDTO> pagedResponse = PagedResponse.of(
                    patientDTOs, page, size, patientsPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách tất cả bệnh nhân thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/{patientCode}")
    public ResponseEntity<ApiResponse<benhNhanDTO>> getPatientByCode(@PathVariable String maBenhNhan) {
        try {
            BenhNhan patient = benhNhanService.layBenhNhanTheoMa(maBenhNhan);
            benhNhanDTO patientDTO = benhNhanMapper.toDTO(patient);

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy thông tin bệnh nhân thành công", patientDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không tìm thấy bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/{patientCode}/surveys")
    public ResponseEntity<ApiResponse<PagedResponse<PhanHoiKhaoSatDTO>>> getPatientSurveyHistory(
            @PathVariable String maBenhNhan,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            // Map English field names to Vietnamese entity attributes
            String mappedSortBy = switch (sortBy) {
                case "createdAt" -> "ngayTao";
                case "updatedAt" -> "ngayCapNhat";
                case "totalScore" -> "tongDiem";
                case "surveyType" -> "loaiKhaoSat";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<PhanHoiKhaoSat> surveysPage = benhNhanService.layLichSuKhaoSatCuaBenhNhan(maBenhNhan, pageable);
            List<PhanHoiKhaoSatDTO> surveyDTOs = phanHoiKhaoSatMapper.toDTOList(surveysPage.getContent());

            PagedResponse<PhanHoiKhaoSatDTO> pagedResponse = PagedResponse.of(
                    surveyDTOs, page, size, surveysPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy lịch sử khảo sát của bệnh nhân thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy lịch sử khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/recent")
    public ResponseEntity<ApiResponse<PagedResponse<PhanHoiKhaoSatDTO>>> getRecentSurveys(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            // Map English field names to Vietnamese entity attributes
            String mappedSortBy = switch (sortBy) {
                case "createdAt" -> "ngayTao";
                case "updatedAt" -> "ngayCapNhat";
                case "totalScore" -> "tongDiem";
                case "surveyType" -> "loaiKhaoSat";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<PhanHoiKhaoSat> surveysPage = khaoSatService.layKhaoSatGanDay(hours, pageable);
            List<PhanHoiKhaoSatDTO> surveyDTOs = phanHoiKhaoSatMapper.toDTOList(surveysPage.getContent());

            PagedResponse<PhanHoiKhaoSatDTO> pagedResponse = PagedResponse.of(
                    surveyDTOs, page, size, surveysPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách khảo sát gần đây thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/all")
    public ResponseEntity<ApiResponse<PagedResponse<PhanHoiKhaoSatDTO>>> getAllSurveys(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            // Map English field names to Vietnamese entity attributes
            String mappedSortBy = switch (sortBy) {
                case "createdAt" -> "ngayTao";
                case "updatedAt" -> "ngayCapNhat";
                case "totalScore" -> "tongDiem";
                case "surveyType" -> "loaiKhaoSat";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<PhanHoiKhaoSat> surveysPage = khaoSatService.layTatCaPhanHoiKhaoSat(pageable);
            List<PhanHoiKhaoSatDTO> surveyDTOs = phanHoiKhaoSatMapper.toDTOList(surveysPage.getContent());

            PagedResponse<PhanHoiKhaoSatDTO> pagedResponse = PagedResponse.of(
                    surveyDTOs, page, size, surveysPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách tất cả khảo sát thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/surveys/type/{surveyType}")
    public ResponseEntity<ApiResponse<PagedResponse<PhanHoiKhaoSatDTO>>> getSurveysByType(
            @PathVariable LoaiKhaoSat surveyType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            // Map English field names to Vietnamese entity attributes
            String mappedSortBy = switch (sortBy) {
                case "createdAt" -> "ngayTao";
                case "updatedAt" -> "ngayCapNhat";
                case "totalScore" -> "tongDiem";
                case "surveyType" -> "loaiKhaoSat";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<PhanHoiKhaoSat> surveysPage = khaoSatService.layPhanHoiTheoLoaiKhaoSat(surveyType, pageable);
            List<PhanHoiKhaoSatDTO> surveyDTOs = phanHoiKhaoSatMapper.toDTOList(surveysPage.getContent());

            PagedResponse<PhanHoiKhaoSatDTO> pagedResponse = PagedResponse.of(
                    surveyDTOs, page, size, surveysPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách khảo sát theo loại thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách khảo sát: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/search")
    public ResponseEntity<ApiResponse<PagedResponse<benhNhanDTO>>> searchPatients(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            // Map English field names to Vietnamese entity attributes
            String mappedSortBy = switch (sortBy) {
                case "updatedAt" -> "ngayCapNhat";
                case "createdAt" -> "ngayTao";
                case "fullName" -> "hoTen";
                case "patientCode" -> "maBenhNhan";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<BenhNhan> patientsPage = benhNhanService.timKiemBenhNhanTheoTen(name, pageable);
            List<benhNhanDTO> patientDTOs = benhNhanMapper.toDTOList(patientsPage.getContent());

            PagedResponse<benhNhanDTO> pagedResponse = PagedResponse.of(
                    patientDTOs, page, size, patientsPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Tìm kiếm bệnh nhân thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi tìm kiếm bệnh nhân: " + e.getMessage()));
        }
    }

    // Giữ lại API không phân trang cho dashboard/stats
//    @PostMapping("/patients/{patientCode}/validate")
//    public ResponseEntity<ApiResponse<Boolean>> validatePatientCode(@PathVariable String patientCode) {
//        try {
//            boolean isValid = benhNhanService.validatePatientCode(patientCode);
//            return ResponseEntity.ok(ApiResponse.success(
//                    isValid ? "Mã bệnh nhân hợp lệ" : "Mã bệnh nhân không hợp lệ", isValid));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(
//                    ApiResponse.error("Lỗi khi xác thực mã bệnh nhân: " + e.getMessage()));
//        }
//    }

    @PostMapping("/patients/{patientCode}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivatePatient(@PathVariable String patientCode) {
        try {
            benhNhanService.voHieuHoaBenhNhan(patientCode);
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
            benhNhanService.kichHoatBenhNhan(patientCode);
            return ResponseEntity.ok(ApiResponse.success(
                    "Đã kích hoạt bệnh nhân thành công", "Activated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi kích hoạt bệnh nhân: " + e.getMessage()));
        }
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<ThongKeDashboardDTO>> getDashboardStats() {
        try {
            ThongKeDashboardDTO stats = bangDieuKhienService.getDashboardStats();
            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy thống kê dashboard thành công", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy thống kê: " + e.getMessage()));
        }
    }

    @GetMapping("/patients/summary")
    public ResponseEntity<ApiResponse<PagedResponse<TongHopKhaoSatBenhNhanDTO>>> getPatientSurveySummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "lastSurveyDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<TongHopKhaoSatBenhNhanDTO> summariesPage = benhNhanService.layTongHopKhaoSatBenhNhan(pageable);

            PagedResponse<TongHopKhaoSatBenhNhanDTO> pagedResponse = PagedResponse.of(
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
    public ResponseEntity<ApiResponse<List<TongHopKhaoSatBenhNhanDTO>>> exportPatientSurveySummaries() {
        try {
            List<TongHopKhaoSatBenhNhanDTO> summaries = benhNhanService.layTatCaTongHopKhaoSatBenhNhan();

            return ResponseEntity.ok(ApiResponse.success(
                    "Xuất dữ liệu tổng quan bệnh nhân thành công", summaries));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi xuất dữ liệu: " + e.getMessage()));
        }
    }

}