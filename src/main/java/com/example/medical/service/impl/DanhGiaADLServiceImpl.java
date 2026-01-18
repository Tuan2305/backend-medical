package com.example.medical.service.impl;

import com.example.medical.dto.request.DanhGiaADLRequest;
import com.example.medical.dto.response.DanhGiaADLDTO;
import com.example.medical.entity.BenhNhan;
import com.example.medical.entity.DanhGiaADL;
import com.example.medical.entity.User;
import com.example.medical.repository.BenhNhanRepository;
import com.example.medical.repository.DanhGiaADLRepository;
import com.example.medical.repository.UserRepository;
import com.example.medical.service.DanhGiaADLService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DanhGiaADLServiceImpl implements DanhGiaADLService {

    private final DanhGiaADLRepository danhGiaADLRepository;
    private final BenhNhanRepository benhNhanRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DanhGiaADL taoDanhGia(DanhGiaADLRequest request, String tenDangNhapBacSi) {
        BenhNhan benhNhan = benhNhanRepository.findByMaBenhNhan(request.getMaBenhNhan())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));

        User bacSi = userRepository.findByUsername(tenDangNhapBacSi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        DanhGiaADL danhGia = new DanhGiaADL();
        danhGia.setBenhNhan(benhNhan);
        danhGia.setBacSi(bacSi);

        danhGia.setTamRua(request.getTamRua());
        danhGia.setMacQuanAo(request.getMacQuanAo());
        danhGia.setDiVeSinh(request.getDiVeSinh());
        danhGia.setDiChuyen(request.getDiChuyen());
        danhGia.setKiemSoatTieuTieu(request.getKiemSoatTieuTieu());
        danhGia.setAnUong(request.getAnUong());

        int tongDiem = tinhTongDiem(request);
        danhGia.setTongDiem(tongDiem);
        danhGia.setDienGiai(taoDienGiai(tongDiem));

        return danhGiaADLRepository.save(danhGia);
    }

    @Override
    public DanhGiaADLDTO layDanhGiaTheoId(Long id) {
        DanhGiaADL danhGia = danhGiaADLRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));
        return chuyenSangDTO(danhGia);
    }

    @Override
    public Page<DanhGiaADLDTO> layLichSuDanhGia(String maBenhNhan, Pageable pageable) {
        return danhGiaADLRepository.timTheoMaBenhNhan(maBenhNhan, pageable)
                .map(this::chuyenSangDTO);
    }

    @Override
    public Page<DanhGiaADLDTO> layDanhGiaGanDay(int hours, Pageable pageable) {
        LocalDateTime thoiDiemBatDau = LocalDateTime.now().minusHours(hours);
        return danhGiaADLRepository.timDanhGiaGanDay(thoiDiemBatDau, pageable)
                .map(this::chuyenSangDTO);
    }

    @Override
    public Page<DanhGiaADLDTO> layTatCaDanhGia(Pageable pageable) {
        return danhGiaADLRepository.findAll(pageable)
                .map(this::chuyenSangDTO);
    }

    private int tinhTongDiem(DanhGiaADLRequest request) {
        return (request.getTamRua() != null ? request.getTamRua() : 0) +
               (request.getMacQuanAo() != null ? request.getMacQuanAo() : 0) +
               (request.getDiVeSinh() != null ? request.getDiVeSinh() : 0) +
               (request.getDiChuyen() != null ? request.getDiChuyen() : 0) +
               (request.getKiemSoatTieuTieu() != null ? request.getKiemSoatTieuTieu() : 0) +
               (request.getAnUong() != null ? request.getAnUong() : 0);
    }

    private String taoDienGiai(int tongDiem) {
        if (tongDiem == 6) {
            return "Độc lập hoàn toàn: Bệnh nhân có chức năng tốt, tự chăm sóc bản thân hoàn toàn.";
        } else if (tongDiem == 4 || tongDiem == 5) {
            return "Suy giảm trung bình: Bệnh nhân cần hỗ trợ một số hoạt động cơ bản.";
        } else {
            return "Suy giảm nặng (Phụ thuộc nhiều): Bệnh nhân cần hỗ trợ đáng kể trong các hoạt động hàng ngày.";
        }
    }

    private DanhGiaADLDTO chuyenSangDTO(DanhGiaADL danhGia) {
        DanhGiaADLDTO dto = new DanhGiaADLDTO();
        dto.setId(danhGia.getId());
        dto.setMaBenhNhan(danhGia.getBenhNhan().getMaBenhNhan());
        dto.setTenBenhNhan(danhGia.getBenhNhan().getHoTen());
        dto.setTenBacSi(danhGia.getBacSi().getHoTen());

        dto.setTamRua(danhGia.getTamRua());
        dto.setMacQuanAo(danhGia.getMacQuanAo());
        dto.setDiVeSinh(danhGia.getDiVeSinh());
        dto.setDiChuyen(danhGia.getDiChuyen());
        dto.setKiemSoatTieuTieu(danhGia.getKiemSoatTieuTieu());
        dto.setAnUong(danhGia.getAnUong());

        dto.setTongDiem(danhGia.getTongDiem());
        dto.setDienGiai(danhGia.getDienGiai());
        dto.setNgayTao(danhGia.getNgayTao());
        dto.setNgayCapNhat(danhGia.getNgayCapNhat());

        return dto;
    }
}
