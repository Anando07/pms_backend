package net.java.pms_backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import net.java.pms_backend.entity.Project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {

    private Long id;

    @NotBlank(message = "Project name is required")
    @Size(max = 500, message = "Project name must be at most 500 characters")
    private String projectName;

    @NotNull(message = "Ministry ID is required")
    @Positive(message = "Ministry ID must be a positive number")
    private Long ministryId;

    // Directorate is optional, but if provided must be positive
    @Positive(message = "Directorate ID must be a positive number")
    private Long directorateId;

    @NotNull(message = "Approved start date is required")
    private LocalDate approvedStartDate;

    @NotNull(message = "Approved end date is required")
    private LocalDate approvedEndDate;

    @NotNull(message = "Approved budget is required")
    @DecimalMin(value = "0.01", message = "Approved budget must be greater than 0")
    private BigDecimal approvedBudget;

    // --- REVISED FIELDS (ALL OPTIONAL) ---

    // Removed @PastOrPresent so future revised dates work and optional nulls don't fail
    private LocalDate revisedStartDate;

    private LocalDate revisedEndDate;

    // Optional: Only validates min value if a non-null budget is provided
    @Positive(message = "Revised budget must be greater than 0")
    private BigDecimal revisedBudget;

    // --------------------------------------

    @NotNull(message = "Priority is required")
    private Project.Priority priority;

    @NotNull(message = "Status is required")
    private Project.Status status;

    private List<String> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}