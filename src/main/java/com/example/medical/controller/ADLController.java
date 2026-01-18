package com.example.medical.controller;

import com.example.medical.dto.request.DanhGiaADLRequest;
import com.example.medical.dto.response.ApiResponse;
import com.example.medical.dto.response.DanhGiaADLDTO;
import com.example.medical.dto.response.PagedResponse;
import com.example.medical.entity.DanhGiaADL;
import com.example.medical.service.DanhGiaADLService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adl")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
public class ADLController {

    private final DanhGiaADLService danhGiaADLService;

    @PostMapping("/assess")
    public ResponseEntity<ApiResponse<DanhGiaADL>> taoDanhGia(
            @Valid @RequestBody DanhGiaADLRequest request,
            Authentication authentication) {
        try {
            DanhGiaADL danhGia = danhGiaADLService.taoDanhGia(request, authentication.getName());
            return ResponseEntity.ok(ApiResponse.success("Tạo đánh giá ADL thành công", danhGia));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DanhGiaADLDTO>> layDanhGiaTheoId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success("Thành công", danhGiaADLService.layDanhGiaTheoId(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }

    @GetMapping("/patient/{maBenhNhan}")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaADLDTO>>> layLichSuDanhGia(
            @PathVariable String maBenhNhan,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            String mappedSortBy = sortBy.equals("createdAt") ? "ngayTao" : sortBy.equals("updatedAt") ? "ngayCapNhat" : sortBy;
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), mappedSortBy));
            Page<DanhGiaADLDTO> result = danhGiaADLService.layLichSuDanhGia(maBenhNhan, pageable);
            return ResponseEntity.ok(ApiResponse.success("Thành công", PagedResponse.of(result.getContent(), page, size, result.getTotalElements())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaADLDTO>>> layDanhGiaGanDay(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao"));
            Page<DanhGiaADLDTO> result = danhGiaADLService.layDanhGiaGanDay(hours, pageable);
            return ResponseEntity.ok(ApiResponse.success("Thành công", PagedResponse.of(result.getContent(), page, size, result.getTotalElements())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaADLDTO>>> layTatCaDanhGia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao"));
            Page<DanhGiaADLDTO> result = danhGiaADLService.layTatCaDanhGia(pageable);
            return ResponseEntity.ok(ApiResponse.success("Thành công", PagedResponse.of(result.getContent(), page, size, result.getTotalElements())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }
}
