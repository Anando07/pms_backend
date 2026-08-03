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
                .approvedStartDate(project.getApprovedStartDate())
                .approvedEndDate(project.getApprovedEndDate())
                .approvedBudget(project.getApprovedBudget())
                .revisedStartDate(project.getRevisedStartDate())
                .revisedEndDate(project.getRevisedEndDate())
                .revisedBudget(project.getRevisedBudget())
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
                .approvedStartDate(projectDto.getApprovedStartDate())
                .approvedEndDate(projectDto.getApprovedEndDate())
                .approvedBudget(projectDto.getApprovedBudget())
                .revisedStartDate(projectDto.getRevisedStartDate())
                .revisedEndDate(projectDto.getRevisedEndDate())
                .revisedBudget(projectDto.getRevisedBudget())
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
        existing.setApprovedStartDate(projectDto.getApprovedStartDate());
        existing.setApprovedEndDate(projectDto.getApprovedEndDate());
        existing.setApprovedBudget(projectDto.getApprovedBudget());
        // Only update revised fields if explicitly provided in the DTO. This prevents
        // accidental clearing of existing revised values when the client omits them.
        if (projectDto.getRevisedStartDate() != null) {
            existing.setRevisedStartDate(projectDto.getRevisedStartDate());
        }
        if (projectDto.getRevisedEndDate() != null) {
            existing.setRevisedEndDate(projectDto.getRevisedEndDate());
        }
        if (projectDto.getRevisedBudget() != null) {
            existing.setRevisedBudget(projectDto.getRevisedBudget());
        }
        existing.setPriority(projectDto.getPriority());
        existing.setStatus(projectDto.getStatus());
        if (projectDto.getImages() != null) {
            existing.setImages(projectDto.getImages());
        }
    }
}
