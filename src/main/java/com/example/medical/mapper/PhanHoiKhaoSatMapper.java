package com.example.medical.mapper;

import com.example.medical.dto.response.PhanHoiKhaoSatDTO;
import com.example.medical.entity.PhanHoiKhaoSat;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhanHoiKhaoSatMapper {

    public PhanHoiKhaoSatDTO toDTO(PhanHoiKhaoSat khaosat) {
        if (khaosat == null) {
            return null;
        }

        return new PhanHoiKhaoSatDTO(
            khaosat.getId(),
            khaosat.getBenhNhan() != null ? khaosat.getBenhNhan().getMaBenhNhan() : null,
            khaosat.getBenhNhan() != null ? khaosat.getBenhNhan().getHoTen() : null,
            khaosat.getLoaiKhaoSat(),
            khaosat.getTongDiem(),
            khaosat.getDanhGia(),
            khaosat.getNgayTao(),
            khaosat.getCauTraLoi()
        );
    }

    public List<PhanHoiKhaoSatDTO> toDTOList(List<PhanHoiKhaoSat> khaosats) {
        if (khaosats == null) {
            return List.of();
        }
        return khaosats.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PhanHoiKhaoSat toEntity(PhanHoiKhaoSatDTO dto) {
        if (dto == null) {
            return null;
        }

        PhanHoiKhaoSat khaosat = new PhanHoiKhaoSat();
        khaosat.setId(dto.getId());
        // Note: BenhNhan entity needs to be set separately, not from DTO
        // khaosat.setBenhNhan() should be set by the service layer
        khaosat.setLoaiKhaoSat(dto.getLoaiKhaoSat());
        khaosat.setTongDiem(dto.getTongDiem());
        khaosat.setDanhGia(dto.getDanhGia());
        khaosat.setNgayTao(dto.getNgayTao());
        khaosat.setCauTraLoi(dto.getPhanHoi());

        return khaosat;
    }
}