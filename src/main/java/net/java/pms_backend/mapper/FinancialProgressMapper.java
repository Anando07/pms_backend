package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.FinancialProgressDto;
import net.java.pms_backend.entity.FinancialProgress;
import net.java.pms_backend.entity.Project;

public class FinancialProgressMapper {

    private FinancialProgressMapper() {}

    public static FinancialProgressDto mapToDto(FinancialProgress entity) {
        if (entity == null) return null;

        Project project = entity.getProject();
        return FinancialProgressDto.builder()
                .id(entity.getId())
                .projectId(project != null ? project.getId() : null)
                .projectName(project != null ? project.getProjectName() : null)
                .expenseAmount(entity.getExpenseAmount())
                .purpose(entity.getPurpose())
                .expenseDate(entity.getExpenseDate())
                .totalBudget(project != null ? project.getEffectiveBudget() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static FinancialProgress mapToEntity(FinancialProgressDto dto, Project project) {
        if (dto == null) return null;

        return FinancialProgress.builder()
                .project(project)
                .expenseAmount(dto.getExpenseAmount())
                .purpose(dto.getPurpose())
                .expenseDate(dto.getExpenseDate())
                .build();
    }
}