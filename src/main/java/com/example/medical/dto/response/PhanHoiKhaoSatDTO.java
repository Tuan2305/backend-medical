package com.example.medical.dto.response;

import com.example.medical.entity.enu.LoaiKhaoSat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhanHoiKhaoSatDTO {
    private Long id;
    private String maBenhNhan;
    private String tenBenhNhan;
    private LoaiKhaoSat loaiKhaoSat;
    private Integer tongDiem;
    private String danhGia;
    private LocalDateTime ngayTao;
    private String phanHoi;
}