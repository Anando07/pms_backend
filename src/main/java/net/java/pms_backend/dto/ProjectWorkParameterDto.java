package net.java.pms_backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectWorkParameterDto {

    private Long id;

    @NotBlank(message = "Parameter name is required")
    private String parameterName;

    @NotNull(message = "Weightage percentage is required")
    @DecimalMin(value = "0.01", message = "Weightage must be greater than 0")
    @DecimalMax(value = "100.00", message = "Weightage cannot exceed 100%")
    private BigDecimal weightagePercentage;

    private BigDecimal alreadyCompletedPercentage;
}