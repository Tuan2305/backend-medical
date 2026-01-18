package com.example.medical.service;

import com.example.medical.dto.request.GuiKhaoSatRequest;
import com.example.medical.entity.PhanHoiKhaoSat;
import com.example.medical.entity.enu.LoaiKhaoSat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KhaoSatService {

    PhanHoiKhaoSat guiKhaoSat(GuiKhaoSatRequest request);

    // List<PhanHoiKhaoSat> layTatCaPhanHoiKhaoSat();
    Page<PhanHoiKhaoSat> layTatCaPhanHoiKhaoSat(Pageable pageable);

    // List<PhanHoiKhaoSat> layPhanHoiTheoBenhNhan(Long idBenhNhan);
    Page<PhanHoiKhaoSat> layPhanHoiTheoBenhNhan(Long idBenhNhan, Pageable pageable);

    // List<PhanHoiKhaoSat> layPhanHoiTheoLoaiKhaoSat(LoaiKhaoSat loaiKhaoSat);
    Page<PhanHoiKhaoSat> layPhanHoiTheoLoaiKhaoSat(LoaiKhaoSat loaiKhaoSat, Pageable pageable);

    // List<PhanHoiKhaoSat> layKhaoSatGanDay(int soGio);
    Page<PhanHoiKhaoSat> layKhaoSatGanDay(int soGio, Pageable pageable);
}
