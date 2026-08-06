package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.PhysicalProgressDto;
import net.java.pms_backend.dto.ProjectWorkParameterDto;
import net.java.pms_backend.entity.PhysicalProgress;
import net.java.pms_backend.entity.Project;
import net.java.pms_backend.entity.ProjectWorkParameter;

public class PhysicalProgressMapper {

    private PhysicalProgressMapper() {
    }

    public static ProjectWorkParameterDto mapParameterToDto(ProjectWorkParameter param) {
        return ProjectWorkParameterDto.builder()
                .id(param.getId())
                .projectId(param.getProject() != null ? param.getProject().getId() : null)
                .parameterName(param.getParameterName())
                .weightagePercentage(param.getWeightagePercentage())
                .build();
    }

    public static PhysicalProgress mapToEntity(PhysicalProgressDto dto, Project project, ProjectWorkParameter param) {
        return PhysicalProgress.builder()
                .project(project)
                .projectWorkParameter(param)
                .progressDate(dto.getProgressDate())
                .completedPercentage(dto.getCompletedPercentage())
                .remarks(dto.getRemarks())
                .build();
    }

    public static PhysicalProgressDto mapToDto(PhysicalProgress entity) {
        return PhysicalProgressDto.builder()
                .id(entity.getId())
                .projectId(entity.getProject() != null ? entity.getProject().getId() : null)
                .projectName(entity.getProject() != null ? entity.getProject().getProjectName() : null)
                .projectWorkParameterId(
                        entity.getProjectWorkParameter() != null ? entity.getProjectWorkParameter().getId() : null)
                .parameterName(
                        entity.getProjectWorkParameter() != null ? entity.getProjectWorkParameter().getParameterName() : null)
                .progressDate(entity.getProgressDate())
                .completedPercentage(entity.getCompletedPercentage())
                .remarks(entity.getRemarks())
                .build();
    }
}