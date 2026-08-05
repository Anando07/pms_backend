package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.ReceivedFundDto;
import net.java.pms_backend.entity.Project;
import net.java.pms_backend.entity.ReceivedFund;

public class ReceivedFundMapper {

    public static ReceivedFundDto mapToReceivedFundDto(ReceivedFund entity) {
        if (entity == null) return null;

        return ReceivedFundDto.builder()
                .id(entity.getId())
                .projectId(entity.getProject() != null ? entity.getProject().getId() : null)
                .projectName(entity.getProject() != null ? entity.getProject().getProjectName() : null)
                .totalProjectFund(entity.getProject() != null ? entity.getProject().getApprovedBudget() : null) // Adjust getter name if different
                .fundAmount(entity.getFundAmount())
                .fiscalYear(entity.getFiscalYear())
                .receivedDate(entity.getReceivedDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static ReceivedFund mapToReceivedFundEntity(ReceivedFundDto dto, Project project) {
        if (dto == null) return null;

        return ReceivedFund.builder()
                .id(dto.getId())
                .project(project)
                .fundAmount(dto.getFundAmount())
                .fiscalYear(dto.getFiscalYear())
                .receivedDate(dto.getReceivedDate())
                .build();
    }
}