package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.DirectorDto;
import net.java.pms_backend.entity.Director;
import net.java.pms_backend.entity.Directorate;
import net.java.pms_backend.entity.Ministry;
import net.java.pms_backend.entity.Project;

public class DirectorMapper {

    public static DirectorDto mapToDirectorDto(Director entity) {
        if (entity == null) return null;

        return DirectorDto.builder()
                .id(entity.getId())
                .dirName(entity.getDirName())
                .dirDesignation(entity.getDirDesignation())
                .ministryId(entity.getMinistry() != null ? entity.getMinistry().getId() : null)
                .ministryName(entity.getMinistry() != null ? entity.getMinistry().getMinName() : null)
                .directorateId(entity.getDirectorate() != null ? entity.getDirectorate().getId() : null)
                .directorateName(entity.getDirectorate() != null ? entity.getDirectorate().getDirName() : "—")
                .contact(entity.getContact())
                .email(entity.getEmail())
                .projectId(entity.getProject() != null ? entity.getProject().getId() : null)
                .projectName(entity.getProject() != null ? entity.getProject().getProjectName() : null)
                .dutyRole(entity.getDutyRole() != null ? entity.getDutyRole().getLabel() : null)
                .assignedDate(entity.getAssignedDate())
                .releaseDate(entity.getReleaseDate())
                .image(entity.getImage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Director mapToDirectorEntity(DirectorDto dto, Ministry ministry, Directorate directorate, Project project) {
        if (dto == null) return null;

        return Director.builder()
                .id(dto.getId())
                .dirName(dto.getDirName())
                .dirDesignation(dto.getDirDesignation())
                .ministry(ministry)
                .directorate(directorate)
                .contact(dto.getContact())
                .email(dto.getEmail())
                .project(project)
                .dutyRole(Director.DutyRole.fromLabel(dto.getDutyRole()))
                .assignedDate(dto.getAssignedDate())
                .releaseDate(dto.getReleaseDate())
                .image(dto.getImage())
                .build();
    }
}