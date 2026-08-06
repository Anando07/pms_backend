package net.java.pms_backend.service;

import net.java.pms_backend.dto.PhysicalProgressDto;
import net.java.pms_backend.dto.ProjectWorkParameterDto;

import java.util.List;

public interface PhysicalProgressService {

    List<ProjectWorkParameterDto> saveProjectWorkParameters(Long projectId, List<ProjectWorkParameterDto> parameters);

    List<ProjectWorkParameterDto> getProjectWorkParameters(Long projectId);

    ProjectWorkParameterDto updateProjectWorkParameter(Long parameterId, ProjectWorkParameterDto dto);

    void deleteProjectWorkParameter(Long parameterId);

    PhysicalProgressDto createProgress(PhysicalProgressDto dto);

    PhysicalProgressDto getProgressById(Long id);

    List<PhysicalProgressDto> getAllProgress();

    List<PhysicalProgressDto> getProgressByProjectId(Long projectId);

    PhysicalProgressDto updateProgress(Long id, PhysicalProgressDto dto);

    void deleteProgress(Long id);
}