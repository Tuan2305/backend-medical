package com.example.medical.entity;

import com.example.medical.entity.enu.GioiTinh;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor  // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor với tất cả tham số
public class BenhNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "patient_code", unique = true, nullable = false)
    private String maBenhNhan;          // patientCode

    @NotBlank
    @Column(name = "full_name")
    private String hoTen;               // fullName

    @Column(name = "birth_year")
    private Integer namSinh;            // birthYear

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GioiTinh gioiTinh;            // gender

    @Column(name = "address")
    private String diaChi;              // address

    @Column(name = "occupation")
    private String ngheNghiep;          // occupation

    @Column(name = "created_at")
    private LocalDateTime ngayTao = LocalDateTime.now();   // createdAt

    @Column(name = "updated_at")
    private LocalDateTime ngayCapNhat = LocalDateTime.now(); // updatedAt

    @Column(name = "is_active")
    private Boolean dangHoatDong = true; // isActive

    @OneToMany(mappedBy = "benhNhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PhanHoiKhaoSat> danhSachKhaoSat;

    @PreUpdate
    public void preUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Lấy số lượng khảo sát (không load toàn bộ list)
    @JsonIgnore
    public int getSoLuongKhaoSat() {
        return danhSachKhaoSat != null ? danhSachKhaoSat.size() : 0;
    }
}
