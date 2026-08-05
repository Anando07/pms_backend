package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.PhysicalProgressDto;
import net.java.pms_backend.dto.ProjectWorkParameterDto;
import net.java.pms_backend.entity.PhysicalProgress;
import net.java.pms_backend.entity.Project;
import net.java.pms_backend.entity.ProjectWorkParameter; // <--- Crucial Import

public class PhysicalProgressMapper {

    private PhysicalProgressMapper() {}

    public static PhysicalProgressDto mapToDto(PhysicalProgress entity) {
        if (entity == null) return null;

        Project project = entity.getProject();
        ProjectWorkParameter param = entity.getProjectWorkParameter();

        return PhysicalProgressDto.builder()
                .id(entity.getId())
                .projectId(project != null ? project.getId() : null)
                .projectName(project != null ? project.getProjectName() : null)
                .projectWorkParameterId(param != null ? param.getId() : null)
                .parameterName(param != null ? param.getParameterName() : null)
                .targetWeightagePercentage(param != null ? param.getWeightagePercentage() : null)
                .progressDate(entity.getProgressDate())
                .completedPercentage(entity.getCompletedPercentage())
                .remarks(entity.getRemarks())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static PhysicalProgress mapToEntity(PhysicalProgressDto dto, Project project, ProjectWorkParameter param) {
        if (dto == null) return null;

        return PhysicalProgress.builder()
                .project(project)
                .projectWorkParameter(param)
                .progressDate(dto.getProgressDate())
                .completedPercentage(dto.getCompletedPercentage())
                .remarks(dto.getRemarks())
                .build();
    }

    public static ProjectWorkParameterDto mapParameterToDto(ProjectWorkParameter entity) {
        if (entity == null) return null;
        return ProjectWorkParameterDto.builder()
                .id(entity.getId())
                .parameterName(entity.getParameterName())
                .weightagePercentage(entity.getWeightagePercentage())
                .build();
    }
}