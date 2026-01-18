package com.example.medical.repository;

import com.example.medical.entity.DanhGiaSuyYeu;
import com.example.medical.entity.enu.LoaiDanhGiaSuyYeu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DanhGiaSuyYeuRepository extends JpaRepository<DanhGiaSuyYeu, Long> {

    // Tìm theo mã bệnh nhân
    @Query("""
        SELECT d FROM DanhGiaSuyYeu d
        WHERE d.benhNhan.maBenhNhan = :maBenhNhan
    """)
    Page<DanhGiaSuyYeu> timTheoMaBenhNhan(
            @Param("maBenhNhan") String maBenhNhan,
            Pageable pageable
    );

    // Lấy đánh giá gần đây
    @Query("""
        SELECT d FROM DanhGiaSuyYeu d
        WHERE d.ngayTao >= :thoiDiemBatDau
    """)
    Page<DanhGiaSuyYeu> timDanhGiaGanDay(
            @Param("thoiDiemBatDau") LocalDateTime thoiDiemBatDau,
            Pageable pageable
    );

    // Tìm theo loại đánh giá
    @Query("""
        SELECT d FROM DanhGiaSuyYeu d
        WHERE d.loaiDanhGia = :loaiDanhGia
    """)
    Page<DanhGiaSuyYeu> timTheoLoaiDanhGia(
            @Param("loaiDanhGia") LoaiDanhGiaSuyYeu loaiDanhGia,
            Pageable pageable
    );

    // Đếm số lượng đánh giá của bệnh nhân
    long countByBenhNhanMaBenhNhan(String maBenhNhan);

    // Lấy đánh giá mới nhất của bệnh nhân
    @Query("""
        SELECT d FROM DanhGiaSuyYeu d
        WHERE d.benhNhan.maBenhNhan = :maBenhNhan
        ORDER BY d.ngayTao DESC
        LIMIT 1
    """)
    DanhGiaSuyYeu timDanhGiaMoiNhat(@Param("maBenhNhan") String maBenhNhan);
}
