package com.example.medical.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DanhGiaADLRequest {

    @NotBlank(message = "Mã bệnh nhân không được để trống")
    private String maBenhNhan;

    @Min(0) @Max(1)
    private Integer tamRua;

    @Min(0) @Max(1)
    private Integer macQuanAo;

    @Min(0) @Max(1)
    private Integer diVeSinh;

    @Min(0) @Max(1)
    private Integer diChuyen;

    @Min(0) @Max(1)
    private Integer kiemSoatTieuTieu;

    @Min(0) @Max(1)
    private Integer anUong;
}
