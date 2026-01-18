package com.example.medical.entity;

import com.example.medical.entity.enu.LoaiKhaoSat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "survey_responses")
@Getter
@Setter
public class PhanHoiKhaoSat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bệnh nhân
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private BenhNhan benhNhan;

    // Bác sĩ
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "doctor_id")
    private User bacSi;

    @Enumerated(EnumType.STRING)
    @Column(name = "survey_type")
    private LoaiKhaoSat loaiKhaoSat;

    @Column(name = "interpretation", columnDefinition = "TEXT")
    private String cauTraLoi; // Diễn giải/câu trả lời khảo sát

    @Column(name = "total_score")
    private Integer tongDiem;

    @Transient // Không map vào DB, được tính toán từ tongDiem
    private String danhGia;

    @Column(name = "created_at")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime ngayCapNhat = LocalDateTime.now();

    public PhanHoiKhaoSat() {}

    @PreUpdate
    public void preUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }

}


