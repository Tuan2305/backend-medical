package com.example.medical.controller;

import com.example.medical.dto.request.GuiKhaoSatRequest;
import com.example.medical.dto.response.ApiResponse;
import com.example.medical.dto.response.GuiKhaoSatResponse;
import com.example.medical.entity.PhanHoiKhaoSat;
import com.example.medical.entity.enu.LoaiKhaoSat;
import com.example.medical.service.BenhNhanService;
import com.example.medical.service.KhaoSatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private KhaoSatService khaoSatService;

    @Autowired
    private BenhNhanService benhNhanService;

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<GuiKhaoSatResponse>> submitSurvey(
            @Valid @RequestBody GuiKhaoSatRequest request) {
        try {
            // Validate patient code first
            if (!benhNhanService.kiemTraMaBenhNhan(request.getMaBenhNhan())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Mã bệnh nhân không đúng định dạng. Mã phải có dạng BN + 6 chữ số (ví dụ: BN123456)"));
            }

            PhanHoiKhaoSat response = khaoSatService.guiKhaoSat(request);

            GuiKhaoSatResponse responseDto = new GuiKhaoSatResponse(
                    response.getId(),
                    response.getBenhNhan().getId(),
                    response.getBenhNhan().getHoTen(),
                    response.getLoaiKhaoSat(),
                    response.getTongDiem(),
                    response.getCauTraLoi(),
                    response.getNgayTao()
            );

            return ResponseEntity.ok(ApiResponse.success("Gửi khảo sát thành công", responseDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Lỗi khi gửi khảo sát: " + e.getMessage()));
        }
    }

    @PostMapping("/validate-patient")
    public ResponseEntity<ApiResponse<String>> validatePatient(
            @RequestParam String maBenhNhan) {
        try {
            if (!benhNhanService.kiemTraMaBenhNhan(maBenhNhan)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Mã bệnh nhân không đúng định dạng"));
            }

            // Check if patient exists
            try {
                benhNhanService.layBenhNhanTheoMa(maBenhNhan);
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
    public ResponseEntity<ApiResponse<LoaiKhaoSat[]>> getSurveyTypes() {
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách loại khảo sát thành công", LoaiKhaoSat.values()));
    }

    // Doctor endpoints moved to DoctorController for better security
}