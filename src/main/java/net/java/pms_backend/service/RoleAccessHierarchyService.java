package net.java.pms_backend.service;

import net.java.pms_backend.dto.RoleAccessHierarchyDto;

import java.util.List;

public interface RoleAccessHierarchyService {
    RoleAccessHierarchyDto createHierarchy(RoleAccessHierarchyDto dto);
    RoleAccessHierarchyDto getHierarchyByRoleType(String roleType);
    List<RoleAccessHierarchyDto> getAllHierarchies();
    RoleAccessHierarchyDto updateHierarchy(Long id, RoleAccessHierarchyDto dto);
    void deleteHierarchy(Long id);
}

