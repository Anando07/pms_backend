package net.java.pms_backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhysicalProgressDto {

    private Long id;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private String projectName;

    @NotNull(message = "Work Parameter ID is required")
    private Long projectWorkParameterId;

    private String parameterName;
    private BigDecimal targetWeightagePercentage;

    @NotNull(message = "Progress date is required")
    private LocalDate progressDate;

    @NotNull(message = "Completed percentage gain is required")
    @DecimalMin(value = "0.01", message = "Gain must be greater than 0")
    @DecimalMax(value = "100.00", message = "Gain cannot exceed 100%")
    private BigDecimal completedPercentage;

    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}