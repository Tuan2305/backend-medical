package com.example.medical.repository;

import com.example.medical.entity.BenhNhan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan, Long> {
    Optional<BenhNhan> findByMaBenhNhan(String maBenhNhan);

    boolean existsByMaBenhNhan(String maBenhNhan);

    Page<BenhNhan> findByHoTenContainingIgnoreCase(String hoTen, Pageable pageable);

    List<BenhNhan> findByHoTenContainingIgnoreCase(String hoTen);

    // Bệnh nhân đang hoạt động (active)
    @Query("""
        SELECT b FROM BenhNhan b
        WHERE b.dangHoatDong = true
    """)
    Page<BenhNhan> timBenhNhanDangHoatDong(Pageable pageable);

    @Query("""
        SELECT b FROM BenhNhan b
        WHERE b.dangHoatDong = true
        ORDER BY b.ngayCapNhat DESC
    """)
    List<BenhNhan> timBenhNhanDangHoatDong();

    // Bệnh nhân tạo gần đây
    @Query("""
        SELECT b FROM BenhNhan b
        WHERE b.ngayTao >= :thoiDiemBatDau
    """)
    Page<BenhNhan> timBenhNhanMoi(
            @Param("thoiDiemBatDau") LocalDateTime thoiDiemBatDau,
            Pageable pageable
    );

    @Query("""
        SELECT b FROM BenhNhan b
        WHERE b.ngayTao >= :thoiDiemBatDau
        ORDER BY b.ngayTao DESC
    """)
    List<BenhNhan> timBenhNhanMoi(
            @Param("thoiDiemBatDau") LocalDateTime thoiDiemBatDau
    );

    // Bệnh nhân có khảo sát gần đây
    @Query("""
        SELECT DISTINCT b
        FROM BenhNhan b
        JOIN b.danhSachKhaoSat ks
        WHERE ks.ngayTao >= :thoiDiemBatDau
    """)
    Page<BenhNhan> timBenhNhanCoKhaoSatGanDay(
            @Param("thoiDiemBatDau") LocalDateTime thoiDiemBatDau,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT b
        FROM BenhNhan b
        JOIN b.danhSachKhaoSat ks
        WHERE ks.ngayTao >= :thoiDiemBatDau
    """)
    List<BenhNhan> timBenhNhanCoKhaoSatGanDay(
            @Param("thoiDiemBatDau") LocalDateTime thoiDiemBatDau
    );

    // Đếm bệnh nhân đang hoạt động
    @Query("SELECT COUNT(b) FROM BenhNhan b WHERE b.dangHoatDong = true")
    long demBenhNhanDangHoatDong();

    // Đếm bệnh nhân mới
    @Query("SELECT COUNT(b) FROM BenhNhan b WHERE b.ngayTao >= :thoiDiemBatDau")
    long demBenhNhanMoi(@Param("thoiDiemBatDau") LocalDateTime thoiDiemBatDau);
}