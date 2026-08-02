package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.ProjectDto;
import net.java.pms_backend.entity.Directorate;
import net.java.pms_backend.entity.Ministry;
import net.java.pms_backend.entity.Project;
import net.java.pms_backend.exception.ResourceNotFoundException;
import net.java.pms_backend.mapper.ProjectMapper;
import net.java.pms_backend.repository.DirectorateRepository;
import net.java.pms_backend.repository.MinistryRepository;
import net.java.pms_backend.repository.ProjectRepository;
import net.java.pms_backend.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final MinistryRepository ministryRepository;
    private final DirectorateRepository directorateRepository;

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        validateDateRange(projectDto);
        Ministry ministry = findMinistryOrThrow(projectDto.getMinistryId());
        Directorate directorate = null;
        if (projectDto.getDirectorateId() != null) {
            directorate = findDirectorateOrThrow(projectDto.getDirectorateId());
            validateDirectorateBelongsToMinistry(directorate, ministry);
        }

        Project project = ProjectMapper.mapToProject(projectDto, ministry, directorate);
        Project savedProject = projectRepository.save(project);
        return ProjectMapper.mapToProjectDto(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(ProjectMapper::mapToProjectDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return ProjectMapper.mapToProjectDto(project);
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        validateDateRange(projectDto);
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        Ministry ministry = findMinistryOrThrow(projectDto.getMinistryId());
        Directorate directorate = null;
        if (projectDto.getDirectorateId() != null) {
            directorate = findDirectorateOrThrow(projectDto.getDirectorateId());
            validateDirectorateBelongsToMinistry(directorate, ministry);
        }

        ProjectMapper.updateProject(existingProject, projectDto, ministry, directorate);
        Project updatedProject = projectRepository.save(existingProject);
        return ProjectMapper.mapToProjectDto(updatedProject);
    }

    @Override
    public void deleteProject(Long id) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        projectRepository.delete(existingProject);
    }

    private Ministry findMinistryOrThrow(Long ministryId) {
        return ministryRepository.findById(ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found with id: " + ministryId));
    }

    private Directorate findDirectorateOrThrow(Long directorateId) {
        return directorateRepository.findById(directorateId)
                .orElseThrow(() -> new ResourceNotFoundException("Directorate not found with id: " + directorateId));
    }

    private void validateDirectorateBelongsToMinistry(Directorate directorate, Ministry ministry) {
        if (!directorate.getMinistry().getId().equals(ministry.getId())) {
            throw new IllegalArgumentException(
                    "Directorate \"" + directorate.getDirName() + "\" does not belong to ministry \"" + ministry.getMinName() + "\"");
        }
    }

    private void validateDateRange(ProjectDto projectDto) {
        if (projectDto.getStartDate() != null && projectDto.getEndDate() != null
                && projectDto.getEndDate().isBefore(projectDto.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}
