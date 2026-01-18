package com.example.medical.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DanhGiaIADLRequest {

    @NotBlank(message = "Mã bệnh nhân không được để trống")
    private String maBenhNhan;

    @Min(0) @Max(1)
    private Integer suDungDienThoai;

    @Min(0) @Max(1)
    private Integer muaSam;

    @Min(0) @Max(1)
    private Integer chuanBiThucAn;

    @Min(0) @Max(1)
    private Integer donDepNhaCua;

    @Min(0) @Max(1)
    private Integer giatGiu;

    @Min(0) @Max(1)
    private Integer phuongTienDiLai;

    @Min(0) @Max(1)
    private Integer suDungThuoc;

    @Min(0) @Max(1)
    private Integer khaNangTaiChinh;
}
