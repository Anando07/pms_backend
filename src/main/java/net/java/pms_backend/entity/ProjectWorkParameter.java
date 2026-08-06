package net.java.pms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "project_work_parameters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "parameter_name"})
})
public class ProjectWorkParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "parameter_name", nullable = false, length = 255)
    private String parameterName;

    @Column(name = "weightage_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal weightagePercentage;
}