package com.example.medical.service.impl;

import com.example.medical.dto.request.DanhGiaIADLRequest;
import com.example.medical.dto.response.DanhGiaIADLDTO;
import com.example.medical.entity.BenhNhan;
import com.example.medical.entity.DanhGiaIADL;
import com.example.medical.entity.User;
import com.example.medical.repository.BenhNhanRepository;
import com.example.medical.repository.DanhGiaIADLRepository;
import com.example.medical.repository.UserRepository;
import com.example.medical.service.DanhGiaIADLService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DanhGiaIADLServiceImpl implements DanhGiaIADLService {

    private final DanhGiaIADLRepository danhGiaIADLRepository;
    private final BenhNhanRepository benhNhanRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DanhGiaIADL taoDanhGia(DanhGiaIADLRequest request, String tenDangNhapBacSi) {
        BenhNhan benhNhan = benhNhanRepository.findByMaBenhNhan(request.getMaBenhNhan())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));

        User bacSi = userRepository.findByUsername(tenDangNhapBacSi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        DanhGiaIADL danhGia = new DanhGiaIADL();
        danhGia.setBenhNhan(benhNhan);
        danhGia.setBacSi(bacSi);

        danhGia.setSuDungDienThoai(request.getSuDungDienThoai());
        danhGia.setMuaSam(request.getMuaSam());
        danhGia.setChuanBiThucAn(request.getChuanBiThucAn());
        danhGia.setDonDepNhaCua(request.getDonDepNhaCua());
        danhGia.setGiatGiu(request.getGiatGiu());
        danhGia.setPhuongTienDiLai(request.getPhuongTienDiLai());
        danhGia.setSuDungThuoc(request.getSuDungThuoc());
        danhGia.setKhaNangTaiChinh(request.getKhaNangTaiChinh());

        int tongDiem = tinhTongDiem(request);
        danhGia.setTongDiem(tongDiem);
        danhGia.setDienGiai(taoDienGiai(tongDiem));

        return danhGiaIADLRepository.save(danhGia);
    }

    @Override
    public DanhGiaIADLDTO layDanhGiaTheoId(Long id) {
        DanhGiaIADL danhGia = danhGiaIADLRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));
        return chuyenSangDTO(danhGia);
    }

    @Override
    public Page<DanhGiaIADLDTO> layLichSuDanhGia(String maBenhNhan, Pageable pageable) {
        return danhGiaIADLRepository.timTheoMaBenhNhan(maBenhNhan, pageable)
                .map(this::chuyenSangDTO);
    }

    @Override
    public Page<DanhGiaIADLDTO> layDanhGiaGanDay(int hours, Pageable pageable) {
        LocalDateTime thoiDiemBatDau = LocalDateTime.now().minusHours(hours);
        return danhGiaIADLRepository.timDanhGiaGanDay(thoiDiemBatDau, pageable)
                .map(this::chuyenSangDTO);
    }

    @Override
    public Page<DanhGiaIADLDTO> layTatCaDanhGia(Pageable pageable) {
        return danhGiaIADLRepository.findAll(pageable)
                .map(this::chuyenSangDTO);
    }

    private int tinhTongDiem(DanhGiaIADLRequest request) {
        return (request.getSuDungDienThoai() != null ? request.getSuDungDienThoai() : 0) +
               (request.getMuaSam() != null ? request.getMuaSam() : 0) +
               (request.getChuanBiThucAn() != null ? request.getChuanBiThucAn() : 0) +
               (request.getDonDepNhaCua() != null ? request.getDonDepNhaCua() : 0) +
               (request.getGiatGiu() != null ? request.getGiatGiu() : 0) +
               (request.getPhuongTienDiLai() != null ? request.getPhuongTienDiLai() : 0) +
               (request.getSuDungThuoc() != null ? request.getSuDungThuoc() : 0) +
               (request.getKhaNangTaiChinh() != null ? request.getKhaNangTaiChinh() : 0);
    }

    private String taoDienGiai(int tongDiem) {
        if (tongDiem >= 7) {
            return "Chức năng tốt: Bệnh nhân độc lập trong các hoạt động sử dụng công cụ.";
        } else if (tongDiem >= 4) {
            return "Suy giảm nhẹ đến trung bình: Bệnh nhân cần hỗ trợ một số hoạt động.";
        } else {
            return "Suy giảm nặng: Bệnh nhân cần hỗ trợ nhiều trong các hoạt động hàng ngày.";
        }
    }

    private DanhGiaIADLDTO chuyenSangDTO(DanhGiaIADL danhGia) {
        DanhGiaIADLDTO dto = new DanhGiaIADLDTO();
        dto.setId(danhGia.getId());
        dto.setMaBenhNhan(danhGia.getBenhNhan().getMaBenhNhan());
        dto.setTenBenhNhan(danhGia.getBenhNhan().getHoTen());
        dto.setTenBacSi(danhGia.getBacSi().getHoTen());

        dto.setSuDungDienThoai(danhGia.getSuDungDienThoai());
        dto.setMuaSam(danhGia.getMuaSam());
        dto.setChuanBiThucAn(danhGia.getChuanBiThucAn());
        dto.setDonDepNhaCua(danhGia.getDonDepNhaCua());
        dto.setGiatGiu(danhGia.getGiatGiu());
        dto.setPhuongTienDiLai(danhGia.getPhuongTienDiLai());
        dto.setSuDungThuoc(danhGia.getSuDungThuoc());
        dto.setKhaNangTaiChinh(danhGia.getKhaNangTaiChinh());

        dto.setTongDiem(danhGia.getTongDiem());
        dto.setDienGiai(danhGia.getDienGiai());
        dto.setNgayTao(danhGia.getNgayTao());
        dto.setNgayCapNhat(danhGia.getNgayCapNhat());

        return dto;
    }
}
