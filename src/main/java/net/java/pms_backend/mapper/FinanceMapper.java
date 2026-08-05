package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.FinanceDto;
import net.java.pms_backend.entity.DevelopmentPartner;
import net.java.pms_backend.entity.Finance;
import net.java.pms_backend.entity.Project;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FinanceMapper {

    public FinanceDto toDto(Finance entity) {
        if (entity == null) return null;

        return FinanceDto.builder()
                .id(entity.getId())
                .projectId(entity.getProject() != null ? entity.getProject().getId() : null)
                .developmentPartnerId(entity.getDevelopmentPartner() != null ? entity.getDevelopmentPartner().getId() : null)
                .projectName(entity.getProject() != null ? entity.getProject().getProjectName() : null)
                .devPartnerName(entity.getDevelopmentPartner() != null ? entity.getDevelopmentPartner().getDevPartnerName() : null)
                .totalApprovedFund(entity.getTotalApprovedFund())
                .totalRevisedFund(entity.getTotalRevisedFund())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Finance toEntity(FinanceDto dto, Project project, DevelopmentPartner devPartner) {
        if (dto == null) return null;

        return Finance.builder()
                .project(project)
                .developmentPartner(devPartner)
                .totalApprovedFund(dto.getTotalApprovedFund() != null ? dto.getTotalApprovedFund() : BigDecimal.ZERO)
                .totalRevisedFund(dto.getTotalRevisedFund() != null ? dto.getTotalRevisedFund() : BigDecimal.ZERO)
                .build();
    }
}