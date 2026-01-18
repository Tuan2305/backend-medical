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
public class GuiKhaoSatResponse {
    private Long phanHoiId;
    private Long benhNhanId;
    private String tenBenhNhan;
    private LoaiKhaoSat loaiKhaoSat;
    private Integer tongDiem;
    private String danhGia;
    private LocalDateTime submittedAt;
}