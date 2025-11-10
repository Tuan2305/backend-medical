package com.example.medical.entity;

import com.example.medical.entity.enu.SurveyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "survey_responses")
public class SurveyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Enumerated(EnumType.STRING)
    private SurveyType surveyType;

    @Column(columnDefinition = "TEXT")
    private String responses; // JSON string of responses

    private Integer totalScore;

    private String interpretation;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors, getters, setters
    public SurveyResponse() {}


}

