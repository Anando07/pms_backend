package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.PhysicalProgressDto;
import net.java.pms_backend.dto.ProjectWorkParameterDto;
import net.java.pms_backend.entity.PhysicalProgress;
import net.java.pms_backend.entity.Project;
import net.java.pms_backend.entity.ProjectWorkParameter;
import net.java.pms_backend.exception.ResourceNotFoundException;
import net.java.pms_backend.mapper.PhysicalProgressMapper;
import net.java.pms_backend.repository.PhysicalProgressRepository;
import net.java.pms_backend.repository.ProjectRepository;
import net.java.pms_backend.repository.ProjectWorkParameterRepository;
import net.java.pms_backend.service.PhysicalProgressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhysicalProgressServiceImpl implements PhysicalProgressService {

    private final PhysicalProgressRepository physicalProgressRepository;
    private final ProjectRepository projectRepository;
    private final ProjectWorkParameterRepository projectWorkParameterRepository;

    @Override
    @Transactional
    public List<ProjectWorkParameterDto> saveProjectWorkParameters(Long projectId, List<ProjectWorkParameterDto> parameters) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // 1. Validate total weightage equals 100%
        BigDecimal totalWeightage = parameters.stream()
                .map(ProjectWorkParameterDto::getWeightagePercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalWeightage.compareTo(new BigDecimal("100.00")) != 0) {
            throw new IllegalArgumentException("Total target parameter weightage must sum to exactly 100%. Current sum: " + totalWeightage + "%");
        }

        // 2. Fetch existing parameter configurations for this project
        List<ProjectWorkParameter> existingParameters = projectWorkParameterRepository.findByProjectId(projectId);

        // 3. Process Upsert (Update existing / Prepare new)
        List<ProjectWorkParameter> toSave = new java.util.ArrayList<>();
        java.util.Set<Long> updatedIds = new java.util.HashSet<>();

        for (ProjectWorkParameterDto dto : parameters) {
            if (dto.getId() != null) {
                // Update existing parameter
                ProjectWorkParameter existing = existingParameters.stream()
                        .filter(p -> p.getId().equals(dto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Parameter not found with ID: " + dto.getId()));

                existing.setParameterName(dto.getParameterName().trim());
                existing.setWeightagePercentage(dto.getWeightagePercentage());
                toSave.add(existing);
                updatedIds.add(existing.getId());
            } else {
                // Insert new parameter
                ProjectWorkParameter newParam = ProjectWorkParameter.builder()
                        .project(project)
                        .parameterName(dto.getParameterName().trim())
                        .weightagePercentage(dto.getWeightagePercentage())
                        .build();
                toSave.add(newParam);
            }
        }

        // 4. Handle parameters removed from form (Delete only if NO progress logged against them)
        List<ProjectWorkParameter> toDelete = existingParameters.stream()
                .filter(p -> !updatedIds.contains(p.getId()))
                .collect(Collectors.toList());

        for (ProjectWorkParameter paramToDelete : toDelete) {
            BigDecimal loggedTotal = physicalProgressRepository.getTotalLoggedByParameterId(paramToDelete.getId());
            if (loggedTotal != null && loggedTotal.compareTo(BigDecimal.ZERO) > 0) {
                throw new IllegalArgumentException(String.format(
                        "Cannot remove parameter '%s' because physical progress (%s%%) has already been logged against it.",
                        paramToDelete.getParameterName(), loggedTotal));
            }
            projectWorkParameterRepository.delete(paramToDelete);
        }

        // 5. Save all new and updated records
        List<ProjectWorkParameter> saved = projectWorkParameterRepository.saveAll(toSave);

        return saved.stream().map(param -> {
            ProjectWorkParameterDto dto = PhysicalProgressMapper.mapParameterToDto(param);
            BigDecimal alreadyDone = physicalProgressRepository.getTotalLoggedByParameterId(param.getId());
            dto.setAlreadyCompletedPercentage(alreadyDone != null ? alreadyDone : BigDecimal.ZERO);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectWorkParameterDto> getProjectWorkParameters(Long projectId) {
        List<ProjectWorkParameter> params = projectWorkParameterRepository.findByProjectId(projectId);

        return params.stream().map(param -> {
            ProjectWorkParameterDto dto = PhysicalProgressMapper.mapParameterToDto(param);
            BigDecimal alreadyDone = physicalProgressRepository.getTotalLoggedByParameterId(param.getId());
            dto.setAlreadyCompletedPercentage(alreadyDone != null ? alreadyDone : BigDecimal.ZERO);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectWorkParameterDto updateProjectWorkParameter(Long parameterId, ProjectWorkParameterDto dto) {
        ProjectWorkParameter existing = projectWorkParameterRepository.findById(parameterId)
                .orElseThrow(() -> new ResourceNotFoundException("Work Parameter not found with ID: " + parameterId));

        Long projectId = existing.getProject().getId();

        // 1. Fetch all existing parameters for this project to check cumulative weightage
        List<ProjectWorkParameter> projectParameters = projectWorkParameterRepository.findByProjectId(projectId);

        // 2. Calculate the projected total weightage after this update
        BigDecimal totalWeightage = BigDecimal.ZERO;
        for (ProjectWorkParameter param : projectParameters) {
            if (param.getId().equals(parameterId)) {
                totalWeightage = totalWeightage.add(dto.getWeightagePercentage());
            } else {
                totalWeightage = totalWeightage.add(param.getWeightagePercentage());
            }
        }

        // 3. Ensure the total sum equals exactly 100.00%
        if (totalWeightage.compareTo(new BigDecimal("100.00")) != 0) {
            throw new IllegalArgumentException("Total target parameter weightage must sum to exactly 100%. Current sum would be: " + totalWeightage + "%");
        }

        // 4. Update and save
        existing.setParameterName(dto.getParameterName().trim());
        existing.setWeightagePercentage(dto.getWeightagePercentage());

        ProjectWorkParameter updated = projectWorkParameterRepository.save(existing);

        ProjectWorkParameterDto responseDto = PhysicalProgressMapper.mapParameterToDto(updated);
        BigDecimal alreadyDone = physicalProgressRepository.getTotalLoggedByParameterId(updated.getId());
        responseDto.setAlreadyCompletedPercentage(alreadyDone != null ? alreadyDone : BigDecimal.ZERO);

        return responseDto;
    }

    @Override
    @Transactional
    public void deleteProjectWorkParameter(Long parameterId) {
        ProjectWorkParameter paramToDelete = projectWorkParameterRepository.findById(parameterId)
                .orElseThrow(() -> new ResourceNotFoundException("Work Parameter not found with ID: " + parameterId));

        BigDecimal loggedTotal = physicalProgressRepository.getTotalLoggedByParameterId(parameterId);
        if (loggedTotal != null && loggedTotal.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException(String.format(
                    "Cannot remove parameter '%s' because physical progress (%s%%) has already been logged against it.",
                    paramToDelete.getParameterName(), loggedTotal));
        }

        projectWorkParameterRepository.delete(paramToDelete);
    }

    @Override
    @Transactional
    public PhysicalProgressDto createProgress(PhysicalProgressDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dto.getProjectId()));

        ProjectWorkParameter param = projectWorkParameterRepository.findById(dto.getProjectWorkParameterId())
                .orElseThrow(() -> new ResourceNotFoundException("Work Parameter not found with ID: " + dto.getProjectWorkParameterId()));

        BigDecimal loggedTotal = physicalProgressRepository.getTotalLoggedByParameterId(dto.getProjectWorkParameterId());
        if (loggedTotal == null) loggedTotal = BigDecimal.ZERO;

        BigDecimal targetLimit = param.getWeightagePercentage();
        BigDecimal newTotal = loggedTotal.add(dto.getCompletedPercentage());

        if (newTotal.compareTo(targetLimit) > 0) {
            BigDecimal maxAllowed = targetLimit.subtract(loggedTotal).max(BigDecimal.ZERO);
            throw new IllegalArgumentException(String.format(
                    "Exceeds target weightage (%s%%)! Already Completed: %s%%, Max Allowed Gain: %s%%",
                    targetLimit, loggedTotal, maxAllowed));
        }

        PhysicalProgress entity = PhysicalProgressMapper.mapToEntity(dto, project, param);
        PhysicalProgress saved = physicalProgressRepository.save(entity);
        return PhysicalProgressMapper.mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PhysicalProgressDto getProgressById(Long id) {
        PhysicalProgress entity = physicalProgressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress record not found with ID: " + id));
        return PhysicalProgressMapper.mapToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhysicalProgressDto> getAllProgress() {
        return physicalProgressRepository.findAll().stream()
                .map(PhysicalProgressMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhysicalProgressDto> getProgressByProjectId(Long projectId) {
        return physicalProgressRepository.findByProjectId(projectId).stream()
                .map(PhysicalProgressMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PhysicalProgressDto updateProgress(Long id, PhysicalProgressDto dto) {
        PhysicalProgress existing = physicalProgressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress record not found with ID: " + id));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dto.getProjectId()));

        ProjectWorkParameter param = projectWorkParameterRepository.findById(dto.getProjectWorkParameterId())
                .orElseThrow(() -> new ResourceNotFoundException("Work Parameter not found with ID: " + dto.getProjectWorkParameterId()));

        BigDecimal loggedOther = physicalProgressRepository.getTotalLoggedByParameterIdExcludingId(
                dto.getProjectWorkParameterId(), id);
        if (loggedOther == null) loggedOther = BigDecimal.ZERO;

        BigDecimal targetLimit = param.getWeightagePercentage();
        BigDecimal newTotal = loggedOther.add(dto.getCompletedPercentage());

        if (newTotal.compareTo(targetLimit) > 0) {
            BigDecimal maxAllowed = targetLimit.subtract(loggedOther).max(BigDecimal.ZERO);
            throw new IllegalArgumentException(String.format(
                    "Exceeds target weightage (%s%%)! Completed in other logs: %s%%, Max Allowed Gain: %s%%",
                    targetLimit, loggedOther, maxAllowed));
        }

        existing.setProject(project);
        existing.setProjectWorkParameter(param);
        existing.setProgressDate(dto.getProgressDate());
        existing.setCompletedPercentage(dto.getCompletedPercentage());
        existing.setRemarks(dto.getRemarks());

        PhysicalProgress updated = physicalProgressRepository.save(existing);
        return PhysicalProgressMapper.mapToDto(updated);
    }

    @Override
    @Transactional
    public void deleteProgress(Long id) {
        if (!physicalProgressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Progress record not found with ID: " + id);
        }
        physicalProgressRepository.deleteById(id);
    }
}