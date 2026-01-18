package com.example.medical.controller;

import com.example.medical.dto.request.DanhGiaIADLRequest;
import com.example.medical.dto.response.ApiResponse;
import com.example.medical.dto.response.DanhGiaIADLDTO;
import com.example.medical.dto.response.PagedResponse;
import com.example.medical.entity.DanhGiaIADL;
import com.example.medical.service.DanhGiaIADLService;
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
@RequestMapping("/api/iadl")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
public class  IADLController {

    private final DanhGiaIADLService danhGiaIADLService;

    @PostMapping("/assess")
    public ResponseEntity<ApiResponse<DanhGiaIADL>> taoDanhGia(
            @Valid @RequestBody DanhGiaIADLRequest request,
            Authentication authentication) {
        try {
            DanhGiaIADL danhGia = danhGiaIADLService.taoDanhGia(request, authentication.getName());
            return ResponseEntity.ok(ApiResponse.success("Tạo đánh giá IADL thành công", danhGia));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DanhGiaIADLDTO>> layDanhGiaTheoId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success("Thành công", danhGiaIADLService.layDanhGiaTheoId(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }

    @GetMapping("/patient/{maBenhNhan}")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaIADLDTO>>> layLichSuDanhGia(
            @PathVariable String maBenhNhan,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            String mappedSortBy = sortBy.equals("createdAt") ? "ngayTao" : sortBy.equals("updatedAt") ? "ngayCapNhat" : sortBy;
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), mappedSortBy));
            Page<DanhGiaIADLDTO> result = danhGiaIADLService.layLichSuDanhGia(maBenhNhan, pageable);
            return ResponseEntity.ok(ApiResponse.success("Thành công", PagedResponse.of(result.getContent(), page, size, result.getTotalElements())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaIADLDTO>>> layDanhGiaGanDay(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao"));
            Page<DanhGiaIADLDTO> result = danhGiaIADLService.layDanhGiaGanDay(hours, pageable);
            return ResponseEntity.ok(ApiResponse.success("Thành công", PagedResponse.of(result.getContent(), page, size, result.getTotalElements())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaIADLDTO>>> layTatCaDanhGia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao"));
            Page<DanhGiaIADLDTO> result = danhGiaIADLService.layTatCaDanhGia(pageable);
            return ResponseEntity.ok(ApiResponse.success("Thành công", PagedResponse.of(result.getContent(), page, size, result.getTotalElements())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi: " + e.getMessage()));
        }
    }
}
