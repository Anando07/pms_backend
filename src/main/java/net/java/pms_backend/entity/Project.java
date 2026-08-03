package net.java.pms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false, length = 500)
    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministry_id", nullable = false)
    private Ministry ministry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directorate_id", nullable = true)
    private Directorate directorate;

    @Column(name = "approved_start_date", nullable = false)
    private LocalDate approvedStartDate;

    @Column(name = "approved_end_date", nullable = false)
    private LocalDate approvedEndDate;

    @Column(name = "approved_budget", nullable = false, precision = 18, scale = 2)
    private BigDecimal approvedBudget;

    @Column(name = "revised_start_date", nullable = true)
    private LocalDate revisedStartDate;

    @Column(name = "revised_end_date", nullable = true)
    private LocalDate revisedEndDate;


    @Column(name = "revised_budget", nullable = true, precision = 18, scale = 2)
    private BigDecimal revisedBudget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "project_images", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "image_data", columnDefinition = "LONGTEXT")
    @Builder.Default
    private List<String> images = new ArrayList<>();

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

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    public enum Status {
        APPROVED, UNAPPROVED
    }
}
