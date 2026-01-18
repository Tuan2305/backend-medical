package com.example.medical.dto.request;

import com.example.medical.entity.enu.LoaiDanhGiaSuyYeu;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO để tạo đánh giá suy yếu
 */
@Data
public class DanhGiaSuyYeuRequest {

    @NotBlank(message = "Mã bệnh nhân không được để trống")
    private String maBenhNhan;

    @NotNull(message = "Loại đánh giá không được để trống")
    private LoaiDanhGiaSuyYeu loaiDanhGia;

    // ===== FRIED PHENOTYPE =====
    private Boolean sutCanKhongChuY;
    private Boolean camGiacKietSuc;
    private Boolean hoatDongThapThe;
    private Boolean diBoChậm;
    private Boolean sucNamTayYeu;

    // ===== CFS =====
    @Min(value = 1, message = "Điểm CFS phải từ 1-9")
    @Max(value = 9, message = "Điểm CFS phải từ 1-9")
    private Integer diemCFS;
}
