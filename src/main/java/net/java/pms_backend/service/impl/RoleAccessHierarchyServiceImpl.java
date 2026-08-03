package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.RoleAccessHierarchyDto;
import net.java.pms_backend.entity.RoleAccessHierarchy;
import net.java.pms_backend.entity.RoleType;
import net.java.pms_backend.mapper.RoleAccessHierarchyMapper;
import net.java.pms_backend.repository.RoleAccessHierarchyRepository;
import net.java.pms_backend.service.RoleAccessHierarchyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleAccessHierarchyServiceImpl implements RoleAccessHierarchyService {

    private final RoleAccessHierarchyRepository repository;

    @Override
    public RoleAccessHierarchyDto createHierarchy(RoleAccessHierarchyDto dto) {
        RoleAccessHierarchy hierarchy = RoleAccessHierarchyMapper.mapToEntity(dto);
        RoleAccessHierarchy saved = repository.save(hierarchy);
        return RoleAccessHierarchyMapper.mapToDto(saved);
    }

    @Override
    public RoleAccessHierarchyDto getHierarchyByRoleType(String roleType) {
        try {
            RoleType type = RoleType.valueOf(roleType);
            RoleAccessHierarchy hierarchy = repository.findByRoleType(type)
                    .orElseThrow(() -> new RuntimeException("Hierarchy not found for role type: " + roleType));
            return RoleAccessHierarchyMapper.mapToDto(hierarchy);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role type: " + roleType);
        }
    }

    @Override
    public List<RoleAccessHierarchyDto> getAllHierarchies() {
        return repository.findAll()
                .stream()
                .map(RoleAccessHierarchyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleAccessHierarchyDto updateHierarchy(Long id, RoleAccessHierarchyDto dto) {
        RoleAccessHierarchy hierarchy = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hierarchy not found with ID: " + id));

        if (dto.getMinistryAccessLevel() != null) {
            hierarchy.setMinistryAccessLevel(
                    RoleAccessHierarchyMapper.mapToEntity(dto).getMinistryAccessLevel()
            );
        }

        if (dto.getProjectAccessLevel() != null) {
            hierarchy.setProjectAccessLevel(
                    RoleAccessHierarchyMapper.mapToEntity(dto).getProjectAccessLevel()
            );
        }

        if (dto.getCanAccessAllministries() != null) {
            hierarchy.setCanAccessAllministries(dto.getCanAccessAllministries());
        }

        if (dto.getCanAccessAllProjects() != null) {
            hierarchy.setCanAccessAllProjects(dto.getCanAccessAllProjects());
        }

        if (dto.getDescription() != null) {
            hierarchy.setDescription(dto.getDescription());
        }

        RoleAccessHierarchy updated = repository.save(hierarchy);
        return RoleAccessHierarchyMapper.mapToDto(updated);
    }

    @Override
    public void deleteHierarchy(Long id) {
        RoleAccessHierarchy hierarchy = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hierarchy not found with ID: " + id));
        repository.delete(hierarchy);
    }
}

