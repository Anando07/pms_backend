package net.java.pms_backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedFundDto {

    private Long id;
    private Long projectId;
    private String projectName;
    private BigDecimal totalProjectFund;
    private BigDecimal fundAmount;
    private String fiscalYear;
    private LocalDate receivedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}