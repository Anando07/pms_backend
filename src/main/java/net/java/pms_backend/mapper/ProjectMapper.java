package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.ProjectDto;
import net.java.pms_backend.entity.Directorate;
import net.java.pms_backend.entity.Ministry;
import net.java.pms_backend.entity.Project;

public class ProjectMapper {

    private ProjectMapper() {}

    public static ProjectDto mapToProjectDto(Project project) {
        if (project == null) return null;
        return ProjectDto.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .ministryId(project.getMinistry() != null ? project.getMinistry().getId() : null)
                .directorateId(project.getDirectorate() != null ? project.getDirectorate().getId() : null)
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .totalBudget(project.getTotalBudget())
                .priority(project.getPriority())
                .status(project.getStatus())
                .images(project.getImages())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public static Project mapToProject(ProjectDto projectDto, Ministry ministry, Directorate directorate) {
        if (projectDto == null) return null;
        return Project.builder()
                .projectName(projectDto.getProjectName())
                .ministry(ministry)
                .directorate(directorate)
                .startDate(projectDto.getStartDate())
                .endDate(projectDto.getEndDate())
                .totalBudget(projectDto.getTotalBudget())
                .priority(projectDto.getPriority())
                .status(projectDto.getStatus())
                .images(projectDto.getImages())
                .build();
    }

    public static void updateProject(Project existing, ProjectDto projectDto, Ministry ministry, Directorate directorate) {
        if (projectDto == null) return;
        existing.setProjectName(projectDto.getProjectName());
        existing.setMinistry(ministry);
        existing.setDirectorate(directorate);
        existing.setStartDate(projectDto.getStartDate());
        existing.setEndDate(projectDto.getEndDate());
        existing.setTotalBudget(projectDto.getTotalBudget());
        existing.setPriority(projectDto.getPriority());
        existing.setStatus(projectDto.getStatus());
        if (projectDto.getImages() != null) {
            existing.setImages(projectDto.getImages());
        }
    }
}
