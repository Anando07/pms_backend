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
    @Size(max = 255, message = "Project name must be at most 255 characters")
    private String projectName;

    @NotNull(message = "Ministry ID is required")
    @Positive(message = "Ministry ID must be a positive number")
    private Long ministryId;

    @Positive(message = "Directorate ID must be a positive number")
    private Long directorateId;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Total budget is required")
    @DecimalMin(value = "0.01", message = "Total budget must be greater than 0")
    private BigDecimal totalBudget;

    @NotNull(message = "Priority is required")
    private Project.Priority priority;

    @NotNull(message = "Status is required")
    private Project.Status status;

    private List<String> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
