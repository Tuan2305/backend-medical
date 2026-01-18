package com.example.medical.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DanhGiaADLDTO {

    private Long id;
    private String maBenhNhan;
    private String tenBenhNhan;
    private String tenBacSi;

    private Integer tamRua;
    private Integer macQuanAo;
    private Integer diVeSinh;
    private Integer diChuyen;
    private Integer kiemSoatTieuTieu;
    private Integer anUong;

    private Integer tongDiem;
    private String dienGiai;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
