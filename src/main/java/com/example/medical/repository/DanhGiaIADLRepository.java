package com.example.medical.repository;

import com.example.medical.entity.DanhGiaIADL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DanhGiaIADLRepository extends JpaRepository<DanhGiaIADL, Long> {

    @Query("""
        SELECT d FROM DanhGiaIADL d
        WHERE d.benhNhan.maBenhNhan = :maBenhNhan
    """)
    Page<DanhGiaIADL> timTheoMaBenhNhan(
            @Param("maBenhNhan") String maBenhNhan,
            Pageable pageable
    );

    @Query("""
        SELECT d FROM DanhGiaIADL d
        WHERE d.ngayTao >= :thoiDiemBatDau
    """)
    Page<DanhGiaIADL> timDanhGiaGanDay(
            @Param("thoiDiemBatDau") LocalDateTime thoiDiemBatDau,
            Pageable pageable
    );

    long countByBenhNhanMaBenhNhan(String maBenhNhan);
}
