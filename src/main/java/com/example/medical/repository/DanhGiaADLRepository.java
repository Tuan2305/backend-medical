package com.example.medical.repository;

import com.example.medical.entity.DanhGiaADL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DanhGiaADLRepository extends JpaRepository<DanhGiaADL, Long> {

    @Query("""
        SELECT d FROM DanhGiaADL d
        WHERE d.benhNhan.maBenhNhan = :maBenhNhan
    """)
    Page<DanhGiaADL> timTheoMaBenhNhan(
            @Param("maBenhNhan") String maBenhNhan,
            Pageable pageable
    );

    @Query("""
        SELECT d FROM DanhGiaADL d
        WHERE d.ngayTao >= :thoiDiemBatDau
    """)
    Page<DanhGiaADL> timDanhGiaGanDay(
            @Param("thoiDiemBatDau") LocalDateTime thoiDiemBatDau,
            Pageable pageable
    );

    long countByBenhNhanMaBenhNhan(String maBenhNhan);
}
