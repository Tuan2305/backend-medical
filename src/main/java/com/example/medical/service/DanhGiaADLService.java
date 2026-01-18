package com.example.medical.service;

import com.example.medical.dto.request.DanhGiaADLRequest;
import com.example.medical.dto.response.DanhGiaADLDTO;
import com.example.medical.entity.DanhGiaADL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DanhGiaADLService {

    DanhGiaADL taoDanhGia(DanhGiaADLRequest request, String tenDangNhapBacSi);

    DanhGiaADLDTO layDanhGiaTheoId(Long id);

    Page<DanhGiaADLDTO> layLichSuDanhGia(String maBenhNhan, Pageable pageable);

    Page<DanhGiaADLDTO> layDanhGiaGanDay(int hours, Pageable pageable);

    Page<DanhGiaADLDTO> layTatCaDanhGia(Pageable pageable);
}
