package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.RoleDto;
import net.java.pms_backend.entity.Role;

@SuppressWarnings("unused")
public class RoleMapper {

    private RoleMapper() {}

    public static RoleDto mapToRoleDto(Role role) {
        if (role == null) return null;

        return RoleDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }

    public static Role mapToRole(RoleDto roleDto) {
        if (roleDto == null) return null;

        return Role.builder()
                .id(roleDto.getId())
                .roleName(roleDto.getRoleName())
                .build();
    }
}