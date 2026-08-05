package net.java.pms_backend.service;

import net.java.pms_backend.dto.PhysicalProgressDto;
import net.java.pms_backend.dto.ProjectWorkParameterDto;

import java.util.List;

public interface PhysicalProgressService {
    // Stage 1: Configure Parameter Targets
    List<ProjectWorkParameterDto> saveProjectWorkParameters(Long projectId, List<ProjectWorkParameterDto> parameters);
    List<ProjectWorkParameterDto> getProjectWorkParameters(Long projectId);

    // Stage 2: Log Dated Progress
    PhysicalProgressDto createProgress(PhysicalProgressDto dto);
    PhysicalProgressDto getProgressById(Long id);
    List<PhysicalProgressDto> getAllProgress();
    List<PhysicalProgressDto> getProgressByProjectId(Long projectId);
    PhysicalProgressDto updateProgress(Long id, PhysicalProgressDto dto);
    void deleteProgress(Long id);
}