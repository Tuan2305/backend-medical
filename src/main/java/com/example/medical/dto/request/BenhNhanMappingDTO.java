package com.example.medical.dto.request;

import com.example.medical.entity.enu.GioiTinh;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BenhNhanMappingDTO {

    private Long id;
    private String maBenhNhan;
    private String hoTen;
    private Integer namSinh;
    private GioiTinh gioiTinh;
    private String diaChi;
    private String ngheNghiep;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private Boolean dangHoatDong;
    private int soLuongKhaoSat;
    private LocalDateTime ngayKhaoSatGanNhat;
}
