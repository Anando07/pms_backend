package net.java.pms_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceDto {

    private Long id;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Development Partner ID is required")
    private Long developmentPartnerId;

    private BigDecimal totalApprovedFund;
    private BigDecimal totalRevisedFund;

    // Optional response fields
    private String projectName;
    private String devPartnerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}