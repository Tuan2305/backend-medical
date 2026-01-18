package com.example.medical.service.impl;

import com.example.medical.dto.response.ThongKeDashboardDTO;
import com.example.medical.repository.BenhNhanRepository;
import com.example.medical.repository.PhanHoiKhaoSatRepository;
import com.example.medical.service.BangDieuKhienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BangDieuKhienServiceImpl implements BangDieuKhienService {

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private PhanHoiKhaoSatRepository phanHoiKhaoSatRepository;

    @Override
    public ThongKeDashboardDTO getDashboardStats() {
        try {
            // Sử dụng COUNT queries thay vì lấy toàn bộ data
            int tongBenhNhan = (int) benhNhanRepository.demBenhNhanDangHoatDong();

            LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusHours(24 * 7);

            int recentKhaoSat24h = (int) phanHoiKhaoSatRepository.demKhaoSatGanDay(twentyFourHoursAgo);
            int recentKhaoSat7d = (int) phanHoiKhaoSatRepository.demKhaoSatGanDay(sevenDaysAgo);
            int benhNhanMoi24h = (int) benhNhanRepository.demBenhNhanMoi(twentyFourHoursAgo);

            return new ThongKeDashboardDTO(
                    tongBenhNhan,
                    recentKhaoSat24h,
                    recentKhaoSat7d,
                    benhNhanMoi24h
            );
        } catch (Exception e) {
            System.err.println("ERROR in getDashboardStats: " + e.getMessage());
            // Fallback với dữ liệu mặc định
            return new ThongKeDashboardDTO(0, 0, 0, 0);
        }
    }
}