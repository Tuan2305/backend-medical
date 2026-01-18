package com.example.medical.service;

import com.example.medical.dto.request.DanhGiaSuyYeuRequest;
import com.example.medical.dto.response.DanhGiaSuyYeuDTO;
import com.example.medical.entity.DanhGiaSuyYeu;
import com.example.medical.entity.enu.LoaiDanhGiaSuyYeu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DanhGiaSuyYeuService {

    /**
     * Tạo đánh giá suy yếu mới
     */
    DanhGiaSuyYeu taoDanhGia(DanhGiaSuyYeuRequest request, String tenDangNhapBacSi);

    /**
     * Lấy đánh giá theo ID
     */
    DanhGiaSuyYeuDTO layDanhGiaTheoId(Long id);

    /**
     * Lấy lịch sử đánh giá của bệnh nhân
     */
    Page<DanhGiaSuyYeuDTO> layLichSuDanhGia(String maBenhNhan, Pageable pageable);

    /**
     * Lấy đánh giá gần đây (trong X giờ)
     */
    Page<DanhGiaSuyYeuDTO> layDanhGiaGanDay(int hours, Pageable pageable);

    /**
     * Lấy đánh giá theo loại
     */
    Page<DanhGiaSuyYeuDTO> layDanhGiaTheoLoai(LoaiDanhGiaSuyYeu loaiDanhGia, Pageable pageable);

    /**
     * Lấy tất cả đánh giá
     */
    Page<DanhGiaSuyYeuDTO> layTatCaDanhGia(Pageable pageable);
}
