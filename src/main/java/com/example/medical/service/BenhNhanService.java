package com.example.medical.service;

import com.example.medical.entity.BenhNhan;
import com.example.medical.entity.PhanHoiKhaoSat;
import com.example.medical.dto.response.TongHopKhaoSatBenhNhanDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BenhNhanService {

    BenhNhan layBenhNhanTheoMa(String maBenhNhan);

    // List<BenhNhan> layBenhNhanGanDay(int soGio);
    Page<BenhNhan> layBenhNhanGanDay(int soGio, Pageable pageable);

    // List<BenhNhan> layTatCaBenhNhanDangHoatDong();
    Page<BenhNhan> layTatCaBenhNhanDangHoatDong(Pageable pageable);

    // List<PhanHoiKhaoSat> layLichSuKhaoSatCuaBenhNhan(String maBenhNhan);
    Page<PhanHoiKhaoSat> layLichSuKhaoSatCuaBenhNhan(String maBenhNhan, Pageable pageable);

    boolean kiemTraMaBenhNhan(String maBenhNhan);

    BenhNhan taoHoacCapNhatBenhNhan(BenhNhan benhNhan);

    // List<BenhNhan> timKiemBenhNhanTheoTen(String ten);
    Page<BenhNhan> timKiemBenhNhanTheoTen(String ten, Pageable pageable);

    void voHieuHoaBenhNhan(String maBenhNhan);
    void kichHoatBenhNhan(String maBenhNhan);

    Page<TongHopKhaoSatBenhNhanDTO> layTongHopKhaoSatBenhNhan(Pageable pageable);
    List<TongHopKhaoSatBenhNhanDTO> layTatCaTongHopKhaoSatBenhNhan();
}
