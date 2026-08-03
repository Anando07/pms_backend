package net.java.pms_backend.service;

import net.java.pms_backend.dto.RoleDto;
import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    RoleDto getRoleById(Long roleId);
    List<RoleDto> getAllRoles();
    RoleDto updateRole(Long roleId, RoleDto roleDto);
    void deleteRole(Long roleId);
}