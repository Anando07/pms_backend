package net.java.pms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "directors")
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dir_name", nullable = false, length = 255)
    private String dirName;

    @Column(name = "dir_designation", nullable = false, length = 255)
    private String dirDesignation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministry_id", nullable = false)
    private Ministry ministry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directorate_id", nullable = true)
    private Directorate directorate;

    @Column(name = "contact", nullable = false, length = 50)
    private String contact;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "duty_role", nullable = false, length = 50)
    private DutyRole dutyRole;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "release_date", nullable = true)
    private LocalDate releaseDate;

    @Lob
    @Column(name = "image", columnDefinition = "LONGTEXT", nullable = true)
    private String image;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum DutyRole {
        ON_DUTY("On Duty"),
        CURRENT_DUTY("Current Duty"),
        ADDITIONAL_DUTY("Additional Duty"),
        RELEASED("Released"),
        TRANSFERRED("Transferred");

        private final String label;

        DutyRole(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static DutyRole fromLabel(String label) {
            for (DutyRole role : values()) {
                if (role.label.equalsIgnoreCase(label) || role.name().equalsIgnoreCase(label)) {
                    return role;
                }
            }
            return ON_DUTY;
        }
    }
}