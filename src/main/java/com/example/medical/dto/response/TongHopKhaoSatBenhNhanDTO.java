package com.example.medical.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.example.medical.entity.enu.LoaiKhaoSat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TongHopKhaoSatBenhNhanDTO {
    private Long benhNhanId;
    private String maBenhNhan;
    private String hoTen;
    private LocalDateTime ngayKhaoSatGanNhat;

    // Survey scores (null if not completed)
    private Integer psqiScore;
    private LocalDateTime psqiDate;

    private Integer beckScore;
    private LocalDateTime beckDate;

    private Integer zungScore;
    private LocalDateTime zungDate;

    private Integer dass21Score;
    private LocalDateTime dass21Date;

    private Integer mmseScore;
    private LocalDateTime mmseDate;

    // Total completed surveys
    private int tongKhaosat;

    public void capNhatDiemVaNgay(
        LoaiKhaoSat loaiKhaoSat,
        Integer diem,
        LocalDateTime ngay) {

    this.ngayKhaoSatGanNhat =
            this.ngayKhaoSatGanNhat == null
                    ? ngay
                    : this.ngayKhaoSatGanNhat.isBefore(ngay)
                        ? ngay
                        : this.ngayKhaoSatGanNhat;

    switch (loaiKhaoSat) {
        case PSQI -> {
            this.psqiScore = diem;
            this.psqiDate = ngay;
        }
        case BECK -> {
            this.beckScore = diem;
            this.beckDate = ngay;
        }
        case ZUNG -> {
            this.zungScore = diem;
            this.zungDate = ngay;
        }
        case DASS21 -> {
            this.dass21Score = diem;
            this.dass21Date = ngay;
        }
        case MMSE -> {
            this.mmseScore = diem;
            this.mmseDate = ngay;
        }
    }
}
}