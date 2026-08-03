package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.RoleDto;
import net.java.pms_backend.entity.Role;
import net.java.pms_backend.mapper.RoleMapper;
import net.java.pms_backend.entity.Permission;
import net.java.pms_backend.repository.RoleRepository;
import net.java.pms_backend.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = RoleMapper.mapToRole(roleDto);
        Role savedRole = roleRepository.save(role);
        return RoleMapper.mapToRoleDto(savedRole);
    }

    @Override
    public RoleDto getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role does not exist with ID: " + roleId));
        return RoleMapper.mapToRoleDto(role);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(RoleMapper::mapToRoleDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto updateRole(Long roleId, RoleDto roleDto) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role does not exist with ID: " + roleId));

        if (roleDto.getRoleName() != null) {
            role.setRoleName(roleDto.getRoleName());
        }

        if (roleDto.getPermissions() != null) {
            // replace permissions
            java.util.Set<Permission> perms = new java.util.HashSet<>();
            for (String p : roleDto.getPermissions()) {
                try {
                    perms.add(Permission.valueOf(p));
                } catch (IllegalArgumentException ignored) {
                }
            }
            role.setPermissions(perms);
        }

        Role updated = roleRepository.save(role);
        return RoleMapper.mapToRoleDto(updated);
    }

    @Override
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role does not exist with ID: " + roleId));
        roleRepository.deleteById(roleId);
    }
}