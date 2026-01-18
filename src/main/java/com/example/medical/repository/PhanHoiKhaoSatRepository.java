package com.example.medical.repository;

import com.example.medical.entity.PhanHoiKhaoSat;
import com.example.medical.entity.enu.LoaiKhaoSat;
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
public interface PhanHoiKhaoSatRepository extends JpaRepository<PhanHoiKhaoSat, Long> {

    // ===== Theo bệnh nhân =====

    List<PhanHoiKhaoSat> findByBenhNhanId(Long benhNhanId);

    Page<PhanHoiKhaoSat> findByBenhNhanId(Long benhNhanId, Pageable pageable);

    List<PhanHoiKhaoSat> findByBenhNhanIdOrderByNgayTaoDesc(Long benhNhanId);

    Page<PhanHoiKhaoSat> findByBenhNhanIdOrderByNgayTaoDesc(Long benhNhanId, Pageable pageable);

    long countByBenhNhanId(Long benhNhanId);

    Optional<PhanHoiKhaoSat>
    findTopByBenhNhanIdAndLoaiKhaoSatOrderByNgayTaoDesc(Long benhNhanId, LoaiKhaoSat loaiKhaoSat);

    // ===== Theo bác sĩ =====

    Page<PhanHoiKhaoSat> findByBacSiId(Long bacSiId, Pageable pageable);

    // ===== Theo loại khảo sát =====

    List<PhanHoiKhaoSat> findByLoaiKhaoSat(LoaiKhaoSat loaiKhaoSat);

    Page<PhanHoiKhaoSat> findByLoaiKhaoSat(LoaiKhaoSat loaiKhaoSat, Pageable pageable);

    // ===== Theo khoảng thời gian =====

    @Query("""
        SELECT ks
        FROM PhanHoiKhaoSat ks
        WHERE ks.ngayTao BETWEEN :tuNgay AND :denNgay
        ORDER BY ks.ngayTao DESC
    """)
    List<PhanHoiKhaoSat> timTheoKhoangNgay(
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay
    );

    @Query("""
        SELECT ks
        FROM PhanHoiKhaoSat ks
        WHERE ks.ngayTao BETWEEN :tuNgay AND :denNgay
        ORDER BY ks.ngayTao DESC
    """)
    Page<PhanHoiKhaoSat> timTheoKhoangNgay(
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay,
            Pageable pageable
    );

    // ===== Theo tên bệnh nhân =====

    @Query("""
        SELECT ks
        FROM PhanHoiKhaoSat ks
        WHERE ks.benhNhan.hoTen LIKE %:hoTen%
    """)
    List<PhanHoiKhaoSat> timTheoTenBenhNhan(@Param("hoTen") String hoTen);

    @Query("""
        SELECT ks
        FROM PhanHoiKhaoSat ks
        WHERE ks.benhNhan.hoTen LIKE %:hoTen%
        ORDER BY ks.ngayTao DESC
    """)
    Page<PhanHoiKhaoSat> timTheoTenBenhNhan(
            @Param("hoTen") String hoTen,
            Pageable pageable
    );

    // ===== Theo mã bệnh nhân =====

    @Query("""
        SELECT ks
        FROM PhanHoiKhaoSat ks
        WHERE ks.benhNhan.maBenhNhan = :maBenhNhan
        ORDER BY ks.ngayTao DESC
    """)
    List<PhanHoiKhaoSat> timTheoMaBenhNhanMoiNhat(
            @Param("maBenhNhan") String maBenhNhan
    );

    // ===== Gần đây =====

    @Query("""
        SELECT ks
        FROM PhanHoiKhaoSat ks
        WHERE ks.ngayTao >= :tuThoiDiem
        ORDER BY ks.ngayTao DESC
    """)
    List<PhanHoiKhaoSat> timKhaoSatGanDay(@Param("tuThoiDiem") LocalDateTime tuThoiDiem);

    @Query("""
        SELECT ks
        FROM PhanHoiKhaoSat ks
        WHERE ks.ngayTao >= :tuThoiDiem
        ORDER BY ks.ngayTao DESC
    """)
    Page<PhanHoiKhaoSat> timKhaoSatGanDay(
            @Param("tuThoiDiem") LocalDateTime tuThoiDiem,
            Pageable pageable
    );

    // ===== Thống kê =====

    @Query("SELECT COUNT(ks) FROM PhanHoiKhaoSat ks WHERE ks.ngayTao >= :tuThoiDiem")
    long demKhaoSatGanDay(@Param("tuThoiDiem") LocalDateTime tuThoiDiem);

    @Query("SELECT COUNT(ks) FROM PhanHoiKhaoSat ks")
    long demTatCaKhaoSat();
}