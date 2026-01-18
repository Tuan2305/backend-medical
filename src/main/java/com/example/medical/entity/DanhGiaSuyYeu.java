package com.example.medical.entity;

import com.example.medical.entity.enu.LoaiDanhGiaSuyYeu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity đánh giá hội chứng suy yếu (Frailty Assessment)
 * Hỗ trợ 2 phương pháp: Fried Phenotype và Clinical Frailty Scale (CFS)
 */
@Entity
@Table(name = "frailty_assessments")
@Getter
@Setter
public class DanhGiaSuyYeu {

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

    // Loại đánh giá: FRIED hoặc CFS
    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type", nullable = false)
    private LoaiDanhGiaSuyYeu loaiDanhGia;

    // ===== FRIED PHENOTYPE (5 tiêu chí) =====
    @Column(name = "fried_weight_loss")
    private Boolean sutCanKhongChuY; // Sụt cân không chủ ý

    @Column(name = "fried_exhaustion")
    private Boolean camGiacKietSuc; // Cảm giác kiệt sức

    @Column(name = "fried_low_activity")
    private Boolean hoatDongThapThe; // Hoạt động thể chất thấp

    @Column(name = "fried_slow_walking")
    private Boolean diBoChậm; // Tốc độ đi bộ chậm

    @Column(name = "fried_weak_grip")
    private Boolean sucNamTayYeu; // Sức nắm tay yếu

    @Column(name = "fried_total_score")
    private Integer diemFried; // Tổng điểm Fried (0-5)

    // ===== CLINICAL FRAILTY SCALE =====
    @Column(name = "cfs_score")
    private Integer diemCFS; // Điểm CFS (1-9)

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

    public DanhGiaSuyYeu() {}
}
