package com.example.medical.service.impl;

import com.example.medical.dto.response.TongHopKhaoSatBenhNhanDTO;
import com.example.medical.entity.BenhNhan;
import com.example.medical.entity.PhanHoiKhaoSat;
import com.example.medical.entity.enu.LoaiKhaoSat;
import com.example.medical.repository.BenhNhanRepository;
import com.example.medical.repository.PhanHoiKhaoSatRepository;
import com.example.medical.service.BenhNhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BenhNhanServiceImpl implements BenhNhanService {

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private PhanHoiKhaoSatRepository phanHoiKhaoSatRepository;

    // ==================== LẤY BỆNH NHÂN ====================
    @Override
    public BenhNhan layBenhNhanTheoMa(String maBenhNhan) {
        return benhNhanRepository.findByMaBenhNhan(maBenhNhan)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy bệnh nhân với mã: " + maBenhNhan));
    }

    // ==================== DANH SÁCH KHÔNG PHÂN TRANG ====================
//    @Override
//    public List<BenhNhan> layBenhNhanGanDay(int soGio) {
//        LocalDateTime thoiDiem = LocalDateTime.now().minusHours(soGio);
//        return benhNhanRepository.timBenhNhanCoKhaoSatGanDay(thoiDiem);
//    }
//
//    @Override
//    public List<BenhNhan> layTatCaBenhNhanDangHoatDong() {
//        return benhNhanRepository.timBenhNhanDangHoatDong();
//    }
//
//    @Override
//    public List<PhanHoiKhaoSat> layLichSuKhaoSatCuaBenhNhan(String maBenhNhan) {
//        BenhNhan benhNhan = layBenhNhanTheoMa(maBenhNhan);
//        return phanHoiKhaoSatRepository
//                .findByBenhNhanIdOrderByNgayTaoDesc(benhNhan.getId());
//    }
//
//    @Override
//    public List<BenhNhan> timKiemBenhNhanTheoTen(String ten) {
//        if (ten == null || ten.trim().isEmpty()) {
//            return List.of();
//        }
//        return benhNhanRepository
//                .findByHoTenContainingIgnoreCase(ten.trim());
//    }

    // ==================== CÓ PHÂN TRANG ====================
    @Override
    public Page<BenhNhan> layBenhNhanGanDay(int soGio, Pageable pageable) {
        LocalDateTime thoiDiem = LocalDateTime.now().minusHours(soGio);
        return benhNhanRepository
                .timBenhNhanCoKhaoSatGanDay(thoiDiem, pageable);
    }

    @Override
    public Page<BenhNhan> layTatCaBenhNhanDangHoatDong(Pageable pageable) {
        return benhNhanRepository.timBenhNhanDangHoatDong(pageable);
    }

    @Override
    public Page<PhanHoiKhaoSat> layLichSuKhaoSatCuaBenhNhan(
            String maBenhNhan, Pageable pageable) {

        BenhNhan benhNhan = layBenhNhanTheoMa(maBenhNhan);
        return phanHoiKhaoSatRepository
                .findByBenhNhanIdOrderByNgayTaoDesc(benhNhan.getId(), pageable);
    }

    @Override
    public Page<BenhNhan> timKiemBenhNhanTheoTen(
            String ten, Pageable pageable) {

        if (ten == null || ten.trim().isEmpty()) {
            return Page.empty();
        }
        return benhNhanRepository
                .findByHoTenContainingIgnoreCase(ten.trim(), pageable);
    }

    // ==================== NGHIỆP VỤ ====================
    @Override
    public boolean kiemTraMaBenhNhan(String maBenhNhan) {
        return maBenhNhan != null
                && maBenhNhan.matches("^BN\\d{6}$");
    }

    @Override
    public BenhNhan taoHoacCapNhatBenhNhan(BenhNhan benhNhan) {
        if (benhNhan.getId() == null) {
            benhNhan.setNgayTao(LocalDateTime.now());
            benhNhan.setDangHoatDong(true);
        }
        benhNhan.setNgayCapNhat(LocalDateTime.now());
        return benhNhanRepository.save(benhNhan);
    }

    @Override
    public void voHieuHoaBenhNhan(String maBenhNhan) {
        BenhNhan benhNhan = layBenhNhanTheoMa(maBenhNhan);
        benhNhan.setDangHoatDong(false);
        benhNhan.setNgayCapNhat(LocalDateTime.now());
        benhNhanRepository.save(benhNhan);
    }

    @Override
    public void kichHoatBenhNhan(String maBenhNhan) {
        BenhNhan benhNhan = layBenhNhanTheoMa(maBenhNhan);
        benhNhan.setDangHoatDong(true);
        benhNhan.setNgayCapNhat(LocalDateTime.now());
        benhNhanRepository.save(benhNhan);
    }

    // ==================== TỔNG HỢP KHẢO SÁT ====================
    @Override
    public Page<TongHopKhaoSatBenhNhanDTO> layTongHopKhaoSatBenhNhan(
            Pageable pageable) {

        Page<BenhNhan> benhNhanPage =
                benhNhanRepository.timBenhNhanDangHoatDong(pageable);

        List<TongHopKhaoSatBenhNhanDTO> danhSach =
                benhNhanPage.getContent().stream()
                        .map(this::chuyenSangDTO)
                        .collect(Collectors.toList());

        return new PageImpl<>(danhSach, pageable,
                benhNhanPage.getTotalElements());
    }


    @Override
    public List<TongHopKhaoSatBenhNhanDTO> layTatCaTongHopKhaoSatBenhNhan() {
        List<BenhNhan> danhSachBenhNhan = benhNhanRepository.timBenhNhanDangHoatDong();

        return danhSachBenhNhan.stream()
                .map(this::chuyenSangDTO)
                .collect(Collectors.toList());
    }



    // ==================== PRIVATE ====================
    private TongHopKhaoSatBenhNhanDTO chuyenSangDTO(BenhNhan benhNhan) {
        TongHopKhaoSatBenhNhanDTO dto = new TongHopKhaoSatBenhNhanDTO();
        dto.setBenhNhanId(benhNhan.getId());
        dto.setMaBenhNhan(benhNhan.getMaBenhNhan());
        dto.setHoTen(benhNhan.getHoTen());

        ganThongTinKhaoSat(dto, benhNhan.getId(), LoaiKhaoSat.PSQI);
        ganThongTinKhaoSat(dto, benhNhan.getId(), LoaiKhaoSat.BECK);
        ganThongTinKhaoSat(dto, benhNhan.getId(), LoaiKhaoSat.ZUNG);
        ganThongTinKhaoSat(dto, benhNhan.getId(), LoaiKhaoSat.DASS21);
        ganThongTinKhaoSat(dto, benhNhan.getId(), LoaiKhaoSat.MMSE);

        dto.setTongKhaosat(
                (int) phanHoiKhaoSatRepository.countByBenhNhanId(benhNhan.getId())
        );

        return dto;
    }

    private void ganThongTinKhaoSat(
            TongHopKhaoSatBenhNhanDTO dto,
            Long idBenhNhan,
            LoaiKhaoSat loaiKhaoSat) {

        Optional<PhanHoiKhaoSat> phanHoi =
                phanHoiKhaoSatRepository
                        .findTopByBenhNhanIdAndLoaiKhaoSatOrderByNgayTaoDesc(
                                idBenhNhan, loaiKhaoSat);

        phanHoi.ifPresent(p -> {
            dto.capNhatDiemVaNgay(loaiKhaoSat,
                    p.getTongDiem(), p.getNgayTao());
        });
    }
}
