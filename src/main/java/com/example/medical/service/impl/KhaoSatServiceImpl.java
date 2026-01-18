package com.example.medical.service.impl;

import com.example.medical.dto.request.GuiKhaoSatRequest;
import com.example.medical.entity.BenhNhan;
import com.example.medical.entity.PhanHoiKhaoSat;
import com.example.medical.entity.enu.LoaiKhaoSat;
import com.example.medical.repository.BenhNhanRepository;
import com.example.medical.repository.PhanHoiKhaoSatRepository;
import com.example.medical.service.KhaoSatService;
import com.example.medical.service.calculator.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class KhaoSatServiceImpl implements KhaoSatService {

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private PhanHoiKhaoSatRepository phanHoiKhaoSatRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Calculator
    @Autowired private TinhDiemDASS21 tinhDiemDASS21;
    @Autowired private TinhDiemBeck tinhDiemBeck;
    @Autowired private TinhDiemZung tinhDiemZung;
    @Autowired private TinhDiemPSQI tinhDiemPSQI;
    @Autowired private TinhDiemMMSE tinhDiemMMSE;

    private Map<LoaiKhaoSat, TinhDiemKhaoSat> banDoTinhDiem;

    @Autowired
    public void khoiTaoBanDoTinhDiem() {
        banDoTinhDiem = new HashMap<>();
        banDoTinhDiem.put(LoaiKhaoSat.DASS21, tinhDiemDASS21);
        banDoTinhDiem.put(LoaiKhaoSat.BECK, tinhDiemBeck);
        banDoTinhDiem.put(LoaiKhaoSat.ZUNG, tinhDiemZung);
        banDoTinhDiem.put(LoaiKhaoSat.PSQI, tinhDiemPSQI);
        banDoTinhDiem.put(LoaiKhaoSat.MMSE, tinhDiemMMSE);
    }

    // ==================== GỬI KHẢO SÁT ====================
    @Override
    public PhanHoiKhaoSat guiKhaoSat(GuiKhaoSatRequest request) {
        try {
            if (!kiemTraMaBenhNhan(request.getMaBenhNhan())) {
                throw new RuntimeException("Mã bệnh nhân không hợp lệ");
            }

            BenhNhan benhNhan = timHoacTaoBenhNhan(request);

            PhanHoiKhaoSat phanHoi = new PhanHoiKhaoSat();
            phanHoi.setBenhNhan(benhNhan);
            phanHoi.setLoaiKhaoSat(request.getLoaiKhaoSat());
            phanHoi.setCauTraLoi(
                    objectMapper.writeValueAsString(request.getCauTraLoi())
            );

            int tongDiem = tinhDiem(request.getLoaiKhaoSat(), request.getCauTraLoi());
            String danhGia = danhGiaKetQua(request.getLoaiKhaoSat(), tongDiem);

            phanHoi.setTongDiem(tongDiem);
            phanHoi.setDanhGia(danhGia);

            PhanHoiKhaoSat ketQua = phanHoiKhaoSatRepository.save(phanHoi);

            benhNhan.setNgayCapNhat(LocalDateTime.now());
            benhNhanRepository.save(benhNhan);

            return ketQua;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gửi khảo sát: " + e.getMessage(), e);
        }
    }

    // ==================== TRUY VẤN ====================
//    @Override
//    public List<PhanHoiKhaoSat> layTatCaPhanHoiKhaoSat() {
//        return phanHoiKhaoSatRepository.findAll();
//    }

    @Override
    public Page<PhanHoiKhaoSat> layKhaoSatGanDay(int soGio, Pageable pageable) {
        LocalDateTime thoiDiem = LocalDateTime.now().minusHours(soGio);
        return phanHoiKhaoSatRepository.timKhaoSatGanDay(thoiDiem, pageable);
    }
//
//    @Override
//    public List<PhanHoiKhaoSat> layKhaoSatGanDay(int soGio) {
//        LocalDateTime thoiDiem = LocalDateTime.now().minusHours(soGio);
//        return phanHoiKhaoSatRepository.findRecentResponses(thoiDiem);
//    }

    @Override
    public Page<PhanHoiKhaoSat> layTatCaPhanHoiKhaoSat(Pageable pageable) {
        return phanHoiKhaoSatRepository.findAll(pageable);
    }

//    @Override
//    public List<PhanHoiKhaoSat> layPhanHoiTheoBenhNhan(Long idBenhNhan) {
//        return List.of();
//    }

    @Override
    public Page<PhanHoiKhaoSat> layPhanHoiTheoBenhNhan(Long idBenhNhan, Pageable pageable) {
        return phanHoiKhaoSatRepository
                .findByBenhNhanIdOrderByNgayTaoDesc(idBenhNhan, pageable);
    }

//    @Override
//    public List<PhanHoiKhaoSat> layPhanHoiTheoLoaiKhaoSat(LoaiKhaoSat loaiKhaoSat) {
//        return List.of();
//    }

    @Override
    public Page<PhanHoiKhaoSat> layPhanHoiTheoLoaiKhaoSat(
            LoaiKhaoSat loaiKhaoSat, Pageable pageable) {
        return phanHoiKhaoSatRepository.findByLoaiKhaoSat(loaiKhaoSat, pageable);
    }

    // ==================== TIỆN ÍCH ====================
    private boolean kiemTraMaBenhNhan(String maBenhNhan) {
        return maBenhNhan != null &&
                maBenhNhan.matches("^BN\\d{6}$");
    }

    private BenhNhan timHoacTaoBenhNhan(GuiKhaoSatRequest request) {
        return benhNhanRepository
                .findByMaBenhNhan(request.getMaBenhNhan())
                .map(benhNhan -> capNhatThongTinBenhNhan(benhNhan, request))
                .orElseGet(() -> taoMoiBenhNhan(request));
    }

    private BenhNhan taoMoiBenhNhan(GuiKhaoSatRequest request) {
        BenhNhan benhNhan = new BenhNhan();
        benhNhan.setMaBenhNhan(request.getMaBenhNhan());
        return capNhatThongTinBenhNhan(benhNhan, request);
    }

    private BenhNhan capNhatThongTinBenhNhan(
            BenhNhan benhNhan, GuiKhaoSatRequest request) {

        benhNhan.setHoTen(request.getHoTen());
        benhNhan.setNamSinh(request.getNamSinh());
        benhNhan.setGioiTinh(request.getGioiTinh());
        benhNhan.setDiaChi(request.getDiaChi());
        benhNhan.setNgheNghiep(request.getNgheNghiep());
        benhNhan.setNgayCapNhat(LocalDateTime.now());

        return benhNhanRepository.save(benhNhan);
    }

    private int tinhDiem(LoaiKhaoSat loaiKhaoSat, Map<String, Object> cauTraLoi) {
        TinhDiemKhaoSat boTinhDiem = banDoTinhDiem.get(loaiKhaoSat);
        return boTinhDiem != null ? boTinhDiem.tinhDiem(cauTraLoi) : 0;
    }

    private String danhGiaKetQua(LoaiKhaoSat loaiKhaoSat, int tongDiem) {
        TinhDiemKhaoSat boTinhDiem = banDoTinhDiem.get(loaiKhaoSat);
        return boTinhDiem != null
                ? boTinhDiem.dienGiaiDiem(tongDiem)
                : "Không có diễn giải cho loại khảo sát này";
    }
}
