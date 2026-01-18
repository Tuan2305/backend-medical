package com.example.medical.controller;

import com.example.medical.dto.request.DanhGiaSuyYeuRequest;
import com.example.medical.dto.response.ApiResponse;
import com.example.medical.dto.response.DanhGiaSuyYeuDTO;
import com.example.medical.dto.response.PagedResponse;
import com.example.medical.entity.DanhGiaSuyYeu;
import com.example.medical.entity.enu.LoaiDanhGiaSuyYeu;
import com.example.medical.service.DanhGiaSuyYeuService;
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

/**
 * Controller quản lý đánh giá hội chứng suy yếu (Frailty Assessment)
 */
@RestController
@RequestMapping("/api/frailty")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
public class FrailtyController {

    private final DanhGiaSuyYeuService danhGiaSuyYeuService;

    /**
     * Tạo đánh giá suy yếu mới
     */
    @PostMapping("/assess")
    public ResponseEntity<ApiResponse<DanhGiaSuyYeu>> taoDanhGia(
            @Valid @RequestBody DanhGiaSuyYeuRequest request,
            Authentication authentication) {
        try {
            String tenDangNhap = authentication.getName();
            DanhGiaSuyYeu danhGia = danhGiaSuyYeuService.taoDanhGia(request, tenDangNhap);
            return ResponseEntity.ok(ApiResponse.success(
                    "Tạo đánh giá suy yếu thành công", danhGia));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi tạo đánh giá: " + e.getMessage()));
        }
    }

    /**
     * Lấy đánh giá theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DanhGiaSuyYeuDTO>> layDanhGiaTheoId(@PathVariable Long id) {
        try {
            DanhGiaSuyYeuDTO danhGia = danhGiaSuyYeuService.layDanhGiaTheoId(id);
            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy đánh giá thành công", danhGia));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy đánh giá: " + e.getMessage()));
        }
    }

    /**
     * Lấy lịch sử đánh giá của bệnh nhân
     */
    @GetMapping("/patient/{maBenhNhan}")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaSuyYeuDTO>>> layLichSuDanhGia(
            @PathVariable String maBenhNhan,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            // Map field names
            String mappedSortBy = switch (sortBy) {
                case "createdAt" -> "ngayTao";
                case "updatedAt" -> "ngayCapNhat";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<DanhGiaSuyYeuDTO> danhGiaPage = danhGiaSuyYeuService.layLichSuDanhGia(maBenhNhan, pageable);
            PagedResponse<DanhGiaSuyYeuDTO> pagedResponse = PagedResponse.of(
                    danhGiaPage.getContent(), page, size, danhGiaPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy lịch sử đánh giá thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy lịch sử: " + e.getMessage()));
        }
    }

    /**
     * Lấy đánh giá gần đây
     */
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaSuyYeuDTO>>> layDanhGiaGanDay(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            String mappedSortBy = switch (sortBy) {
                case "createdAt" -> "ngayTao";
                case "updatedAt" -> "ngayCapNhat";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<DanhGiaSuyYeuDTO> danhGiaPage = danhGiaSuyYeuService.layDanhGiaGanDay(hours, pageable);
            PagedResponse<DanhGiaSuyYeuDTO> pagedResponse = PagedResponse.of(
                    danhGiaPage.getContent(), page, size, danhGiaPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách đánh giá gần đây thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách: " + e.getMessage()));
        }
    }

    /**
     * Lấy đánh giá theo loại (FRIED hoặc CFS)
     */
    @GetMapping("/type/{loaiDanhGia}")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaSuyYeuDTO>>> layDanhGiaTheoLoai(
            @PathVariable LoaiDanhGiaSuyYeu loaiDanhGia,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            String mappedSortBy = switch (sortBy) {
                case "createdAt" -> "ngayTao";
                case "updatedAt" -> "ngayCapNhat";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<DanhGiaSuyYeuDTO> danhGiaPage = danhGiaSuyYeuService.layDanhGiaTheoLoai(loaiDanhGia, pageable);
            PagedResponse<DanhGiaSuyYeuDTO> pagedResponse = PagedResponse.of(
                    danhGiaPage.getContent(), page, size, danhGiaPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy danh sách đánh giá theo loại thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách: " + e.getMessage()));
        }
    }

    /**
     * Lấy tất cả đánh giá
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PagedResponse<DanhGiaSuyYeuDTO>>> layTatCaDanhGia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            String mappedSortBy = switch (sortBy) {
                case "createdAt" -> "ngayTao";
                case "updatedAt" -> "ngayCapNhat";
                default -> sortBy;
            };

            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortBy));

            Page<DanhGiaSuyYeuDTO> danhGiaPage = danhGiaSuyYeuService.layTatCaDanhGia(pageable);
            PagedResponse<DanhGiaSuyYeuDTO> pagedResponse = PagedResponse.of(
                    danhGiaPage.getContent(), page, size, danhGiaPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    "Lấy tất cả đánh giá thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Lỗi khi lấy danh sách: " + e.getMessage()));
        }
    }
}
