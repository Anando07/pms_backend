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
public class FinancialProgressDto {

    private Long id;

    @NotNull(message = "Project ID is required")
    @Positive(message = "Project ID must be a positive number")
    private Long projectId;

    private String projectName;

    @NotNull(message = "Expense amount is required")
    @DecimalMin(value = "0.01", message = "Expense amount must be greater than zero")
    private BigDecimal expenseAmount;

    @NotBlank(message = "Purpose of expense is required")
    @Size(max = 1000, message = "Purpose must not exceed 1000 characters")
    private String purpose;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;

    private BigDecimal totalBudget; // Calculated Effective Budget (Lakhs Tk)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}