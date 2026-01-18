package com.example.medical.mapper;

import com.example.medical.dto.response.benhNhanDTO;
import com.example.medical.entity.BenhNhan;
import com.example.medical.entity.PhanHoiKhaoSat;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BenhNhanMapper {

    public benhNhanDTO toDTO(BenhNhan benhNhan) {
        if (benhNhan == null) {
            return null;
        }

        LocalDateTime ngayKhaoSatCuoi = null;
        int diemKhaoSat = 0;

        if (benhNhan.getDanhSachKhaoSat() != null && !benhNhan.getDanhSachKhaoSat().isEmpty()) {
            diemKhaoSat = benhNhan.getDanhSachKhaoSat().size();
            ngayKhaoSatCuoi = benhNhan.getDanhSachKhaoSat().stream()
                    .map(PhanHoiKhaoSat::getNgayTao)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
        }

        return new benhNhanDTO(
                benhNhan.getId(),
                benhNhan.getMaBenhNhan(),
                benhNhan.getHoTen(),
                benhNhan.getNamSinh(),
                benhNhan.getGioiTinh(),
                benhNhan.getDiaChi(),
                benhNhan.getNgheNghiep(),
                benhNhan.getNgayTao(),
                benhNhan.getNgayCapNhat(),
                benhNhan.getDangHoatDong(),
                diemKhaoSat,
                ngayKhaoSatCuoi
        );
    }

    public List<benhNhanDTO> toDTOList(List<BenhNhan> patients) {
        if (patients == null) {
            return List.of();
        }
        return patients.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BenhNhan toEntity(benhNhanDTO dto) {
        if (dto == null) {
            return null;
        }

        BenhNhan patient = new BenhNhan();
        patient.setId(dto.getId());
        patient.setMaBenhNhan(dto.getMaBenhNhan());
        patient.setHoTen(dto.getHoTen());
        patient.setNamSinh(dto.getNamSinh());
        patient.setGioiTinh(dto.getGioiTinh());
        patient.setDiaChi(dto.getDiaChi());
        patient.setNgheNghiep(dto.getNgheNghiep());
        patient.setNgayTao(dto.getNgayTao());
        patient.setNgayCapNhat(dto.getNgayCapNhat());
        patient.setDangHoatDong(dto.getDangHoatDong());

        return patient;
    }
}