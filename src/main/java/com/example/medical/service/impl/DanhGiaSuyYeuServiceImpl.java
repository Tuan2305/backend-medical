package com.example.medical.service.impl;

import com.example.medical.dto.request.DanhGiaSuyYeuRequest;
import com.example.medical.dto.response.DanhGiaSuyYeuDTO;
import com.example.medical.entity.BenhNhan;
import com.example.medical.entity.DanhGiaSuyYeu;
import com.example.medical.entity.User;
import com.example.medical.entity.enu.LoaiDanhGiaSuyYeu;
import com.example.medical.repository.BenhNhanRepository;
import com.example.medical.repository.DanhGiaSuyYeuRepository;
import com.example.medical.repository.UserRepository;
import com.example.medical.service.DanhGiaSuyYeuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DanhGiaSuyYeuServiceImpl implements DanhGiaSuyYeuService {

    private final DanhGiaSuyYeuRepository danhGiaSuyYeuRepository;
    private final BenhNhanRepository benhNhanRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DanhGiaSuyYeu taoDanhGia(DanhGiaSuyYeuRequest request, String tenDangNhapBacSi) {
        // Tìm bệnh nhân
        BenhNhan benhNhan = benhNhanRepository.findByMaBenhNhan(request.getMaBenhNhan())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với mã: " + request.getMaBenhNhan()));

        // Tìm bác sĩ
        User bacSi = userRepository.findByUsername(tenDangNhapBacSi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        DanhGiaSuyYeu danhGia = new DanhGiaSuyYeu();
        danhGia.setBenhNhan(benhNhan);
        danhGia.setBacSi(bacSi);
        danhGia.setLoaiDanhGia(request.getLoaiDanhGia());

        if (request.getLoaiDanhGia() == LoaiDanhGiaSuyYeu.FRIED) {
            // Xử lý Fried Phenotype
            danhGia.setSutCanKhongChuY(request.getSutCanKhongChuY());
            danhGia.setCamGiacKietSuc(request.getCamGiacKietSuc());
            danhGia.setHoatDongThapThe(request.getHoatDongThapThe());
            danhGia.setDiBoChậm(request.getDiBoChậm());
            danhGia.setSucNamTayYeu(request.getSucNamTayYeu());

            // Tính điểm Fried
            int diemFried = tinhDiemFried(request);
            danhGia.setDiemFried(diemFried);

            // Tạo diễn giải
            danhGia.setDienGiai(tạoDienGiaiFried(diemFried));

        } else if (request.getLoaiDanhGia() == LoaiDanhGiaSuyYeu.CFS) {
            // Xử lý CFS
            danhGia.setDiemCFS(request.getDiemCFS());
            danhGia.setDienGiai(tạoDienGiaiCFS(request.getDiemCFS()));
        }

        return danhGiaSuyYeuRepository.save(danhGia);
    }

    @Override
    public DanhGiaSuyYeuDTO layDanhGiaTheoId(Long id) {
        DanhGiaSuyYeu danhGia = danhGiaSuyYeuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá với ID: " + id));
        return chuyenSangDTO(danhGia);
    }

    @Override
    public Page<DanhGiaSuyYeuDTO> layLichSuDanhGia(String maBenhNhan, Pageable pageable) {
        Page<DanhGiaSuyYeu> danhGiaPage = danhGiaSuyYeuRepository.timTheoMaBenhNhan(maBenhNhan, pageable);
        return danhGiaPage.map(this::chuyenSangDTO);
    }

    @Override
    public Page<DanhGiaSuyYeuDTO> layDanhGiaGanDay(int hours, Pageable pageable) {
        LocalDateTime thoiDiemBatDau = LocalDateTime.now().minusHours(hours);
        Page<DanhGiaSuyYeu> danhGiaPage = danhGiaSuyYeuRepository.timDanhGiaGanDay(thoiDiemBatDau, pageable);
        return danhGiaPage.map(this::chuyenSangDTO);
    }

    @Override
    public Page<DanhGiaSuyYeuDTO> layDanhGiaTheoLoai(LoaiDanhGiaSuyYeu loaiDanhGia, Pageable pageable) {
        Page<DanhGiaSuyYeu> danhGiaPage = danhGiaSuyYeuRepository.timTheoLoaiDanhGia(loaiDanhGia, pageable);
        return danhGiaPage.map(this::chuyenSangDTO);
    }

    @Override
    public Page<DanhGiaSuyYeuDTO> layTatCaDanhGia(Pageable pageable) {
        Page<DanhGiaSuyYeu> danhGiaPage = danhGiaSuyYeuRepository.findAll(pageable);
        return danhGiaPage.map(this::chuyenSangDTO);
    }

    // ===== HELPER METHODS =====

    private int tinhDiemFried(DanhGiaSuyYeuRequest request) {
        int diem = 0;
        if (Boolean.TRUE.equals(request.getSutCanKhongChuY())) diem++;
        if (Boolean.TRUE.equals(request.getCamGiacKietSuc())) diem++;
        if (Boolean.TRUE.equals(request.getHoatDongThapThe())) diem++;
        if (Boolean.TRUE.equals(request.getDiBoChậm())) diem++;
        if (Boolean.TRUE.equals(request.getSucNamTayYeu())) diem++;
        return diem;
    }

    private String tạoDienGiaiFried(int diem) {
        return switch (diem) {
            case 0 -> "Không suy yếu (Robust): Bệnh nhân khỏe mạnh, không có dấu hiệu suy yếu.";
            case 1, 2 -> "Tiền suy yếu (Pre-frail): Bệnh nhân có nguy cơ suy yếu, cần theo dõi và can thiệp sớm.";
            default -> "Suy yếu (Frail): Bệnh nhân đang ở trạng thái suy yếu, cần chăm sóc và hỗ trợ đặc biệt.";
        };
    }

    private String tạoDienGiaiCFS(int diem) {
        return switch (diem) {
            case 1 -> "Rất khỏe mạnh: Năng động, tràn đầy năng lượng, tập thể dục thường xuyên.";
            case 2 -> "Khỏe mạnh: Không có triệu chứng bệnh hoạt động, hoạt động tốt.";
            case 3 -> "Kiểm soát tốt: Có bệnh lý nền nhưng được kiểm soát tốt.";
            case 4 -> "Dễ tổn thương: Hoạt động bị hạn chế vì các vấn đề sức khỏe.";
            case 5 -> "Suy yếu nhẹ: Chậm chạp rõ rệt, cần hỗ trợ trong các hoạt động cần công cụ (IADL).";
            case 6 -> "Suy yếu vừa: Cần hỗ trợ trong mọi hoạt động bên ngoài, khó khăn leo cầu thang.";
            case 7 -> "Suy yếu nặng: Phụ thuộc hoàn toàn về chăm sóc cá nhân.";
            case 8 -> "Rất suy yếu: Phụ thuộc hoàn toàn, giai đoạn cuối đời.";
            case 9 -> "Sắp tử vong: Tiên lượng sống dưới 6 tháng.";
            default -> "Điểm không hợp lệ";
        };
    }

    private DanhGiaSuyYeuDTO chuyenSangDTO(DanhGiaSuyYeu danhGia) {
        DanhGiaSuyYeuDTO dto = new DanhGiaSuyYeuDTO();
        dto.setId(danhGia.getId());
        dto.setMaBenhNhan(danhGia.getBenhNhan().getMaBenhNhan());
        dto.setTenBenhNhan(danhGia.getBenhNhan().getHoTen());
        dto.setTenBacSi(danhGia.getBacSi().getHoTen());
        dto.setLoaiDanhGia(danhGia.getLoaiDanhGia());

        // Fried
        dto.setSutCanKhongChuY(danhGia.getSutCanKhongChuY());
        dto.setCamGiacKietSuc(danhGia.getCamGiacKietSuc());
        dto.setHoatDongThapThe(danhGia.getHoatDongThapThe());
        dto.setDiBoChậm(danhGia.getDiBoChậm());
        dto.setSucNamTayYeu(danhGia.getSucNamTayYeu());
        dto.setDiemFried(danhGia.getDiemFried());

        // CFS
        dto.setDiemCFS(danhGia.getDiemCFS());

        dto.setDienGiai(danhGia.getDienGiai());
        dto.setNgayTao(danhGia.getNgayTao());
        dto.setNgayCapNhat(danhGia.getNgayCapNhat());

        return dto;
    }
}
