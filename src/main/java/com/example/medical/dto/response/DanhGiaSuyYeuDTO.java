package com.example.medical.dto.response;

import com.example.medical.entity.enu.LoaiDanhGiaSuyYeu;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response DTO cho đánh giá suy yếu
 */
@Data
public class DanhGiaSuyYeuDTO {

    private Long id;
    private String maBenhNhan;
    private String tenBenhNhan;
    private String tenBacSi;
    private LoaiDanhGiaSuyYeu loaiDanhGia;

    // Fried
    private Boolean sutCanKhongChuY;
    private Boolean camGiacKietSuc;
    private Boolean hoatDongThapThe;
    private Boolean diBoChậm;
    private Boolean sucNamTayYeu;
    private Integer diemFried;

    // CFS
    private Integer diemCFS;

    // Kết quả
    private String dienGiai;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
