package com.example.medical.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity đánh giá ADL - Katz Scale
 * (Activities of Daily Living - Hoạt động sống cơ bản)
 */
@Entity
@Table(name = "adl_assessments")
@Getter
@Setter
public class DanhGiaADL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User bacSi;

    // 6 tiêu chí ADL (mỗi tiêu chí 0-1 điểm)
    @Column(name = "bathing")
    private Integer tamRua; // 0-1

    @Column(name = "dressing")
    private Integer macQuanAo; // 0-1

    @Column(name = "toileting")
    private Integer diVeSinh; // 0-1

    @Column(name = "transferring")
    private Integer diChuyen; // 0-1

    @Column(name = "continence")
    private Integer kiemSoatTieuTieu; // 0-1

    @Column(name = "feeding")
    private Integer anUong; // 0-1

    // Tổng điểm (0-6)
    @Column(name = "total_score")
    private Integer tongDiem;

    @Column(name = "interpretation", columnDefinition = "TEXT")
    private String dienGiai;

    @Column(name = "created_at")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime ngayCapNhat = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }

    public DanhGiaADL() {}
}
