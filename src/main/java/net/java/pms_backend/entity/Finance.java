package net.java.pms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "finances")
public class Finance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "development_partner_id", nullable = false)
    private DevelopmentPartner developmentPartner;

    @Column(name = "total_approved_fund", precision = 18, scale = 2)
    private BigDecimal totalApprovedFund;

    @Column(name = "total_revised_fund", precision = 18, scale = 2)
    private BigDecimal totalRevisedFund;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (totalApprovedFund == null) totalApprovedFund = BigDecimal.ZERO;
        if (totalRevisedFund == null) totalRevisedFund = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (totalApprovedFund == null) totalApprovedFund = BigDecimal.ZERO;
        if (totalRevisedFund == null) totalRevisedFund = BigDecimal.ZERO;
    }
}