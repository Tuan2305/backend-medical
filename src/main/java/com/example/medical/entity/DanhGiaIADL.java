package com.example.medical.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity đánh giá IADL - Lawton Scale
 * (Instrumental Activities of Daily Living - Hoạt động sinh hoạt sử dụng công cụ)
 */
@Entity
@Table(name = "iadl_assessments")
@Getter
@Setter
public class DanhGiaIADL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bệnh nhân
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private BenhNhan benhNhan;

    // Bác sĩ thực hiện đánh giá
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User bacSi;

    // 8 tiêu chí IADL (mỗi tiêu chí 0-1 điểm)
    @Column(name = "phone_usage")
    private Integer suDungDienThoai; // 0-1

    @Column(name = "shopping")
    private Integer muaSam; // 0-1

    @Column(name = "food_preparation")
    private Integer chuanBiThucAn; // 0-1

    @Column(name = "housekeeping")
    private Integer donDepNhaCua; // 0-1

    @Column(name = "laundry")
    private Integer giatGiu; // 0-1

    @Column(name = "transportation")
    private Integer phuongTienDiLai; // 0-1

    @Column(name = "medication")
    private Integer suDungThuoc; // 0-1

    @Column(name = "finances")
    private Integer khaNangTaiChinh; // 0-1

    // Tổng điểm (0-8)
    @Column(name = "total_score")
    private Integer tongDiem;

    // Diễn giải kết quả
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

    public DanhGiaIADL() {}
}
