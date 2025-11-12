package com.example.medical.entity;

import com.example.medical.entity.enu.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String patientCode;

    @NotBlank
    private String fullName;

    private Integer birthYear;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    private String occupation;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Thêm annotation này để tránh circular reference
    private List<SurveyResponse> surveyResponses;

    public Patient() {}

    public Patient(String patientCode, String fullName, Integer birthYear, Gender gender, String address, String occupation) {
        this.patientCode = patientCode;
        this.fullName = fullName;
        this.birthYear = birthYear;
        this.gender = gender;
        this.address = address;
        this.occupation = occupation;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Thêm method để lấy số lượng surveys thay vì toàn bộ list
    @JsonIgnore
    public int getSurveyCount() {
        return surveyResponses != null ? surveyResponses.size() : 0;
    }
}