package net.java.pms_backend.service;

import net.java.pms_backend.dto.ProjectDto;
import java.util.List;

public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto);
    List<ProjectDto> getAllProjects();
    ProjectDto getProjectById(Long id);
    ProjectDto updateProject(Long id, ProjectDto projectDto);
    void deleteProject(Long id);
}
