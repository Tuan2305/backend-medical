package com.example.medical.service;

import com.example.medical.dto.request.DanhGiaIADLRequest;
import com.example.medical.dto.response.DanhGiaIADLDTO;
import com.example.medical.entity.DanhGiaIADL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DanhGiaIADLService {

    DanhGiaIADL taoDanhGia(DanhGiaIADLRequest request, String tenDangNhapBacSi);

    DanhGiaIADLDTO layDanhGiaTheoId(Long id);

    Page<DanhGiaIADLDTO> layLichSuDanhGia(String maBenhNhan, Pageable pageable);

    Page<DanhGiaIADLDTO> layDanhGiaGanDay(int hours, Pageable pageable);

    Page<DanhGiaIADLDTO> layTatCaDanhGia(Pageable pageable);
}
