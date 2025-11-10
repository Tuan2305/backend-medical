package com.example.medical.entity;

import com.example.medical.entity.enu.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    @NotBlank
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role = Role.DOCTOR;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<SurveyResponse> surveyResponses;

    // Constructors, getters, setters
    public User() {}

    public User(String username, String password, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }
}
