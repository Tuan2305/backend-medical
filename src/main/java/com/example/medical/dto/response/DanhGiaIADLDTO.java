package com.example.medical.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DanhGiaIADLDTO {

    private Long id;
    private String maBenhNhan;
    private String tenBenhNhan;
    private String tenBacSi;

    private Integer suDungDienThoai;
    private Integer muaSam;
    private Integer chuanBiThucAn;
    private Integer donDepNhaCua;
    private Integer giatGiu;
    private Integer phuongTienDiLai;
    private Integer suDungThuoc;
    private Integer khaNangTaiChinh;

    private Integer tongDiem;
    private String dienGiai;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
