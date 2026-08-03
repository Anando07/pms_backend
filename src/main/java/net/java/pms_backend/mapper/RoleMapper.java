package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.RoleDto;
import net.java.pms_backend.entity.Permission;
import net.java.pms_backend.entity.Role;
import net.java.pms_backend.entity.RoleType;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class RoleMapper {

    private RoleMapper() {}

    public static RoleDto mapToRoleDto(Role role) {
        if (role == null) return null;

        Set<String> perms = null;
        if (role.getPermissions() != null) {
            perms = new HashSet<>();
            for (Permission p : role.getPermissions()) {
                perms.add(p.name());
            }
        }

        return RoleDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .roleType(role.getRoleType() != null ? role.getRoleType().name() : null)
                .permissions(perms)
                .build();
    }

    public static Role mapToRole(RoleDto roleDto) {
        if (roleDto == null) return null;

        RoleType roleType = null;
        if (roleDto.getRoleType() != null) {
            try {
                roleType = RoleType.valueOf(roleDto.getRoleType());
            } catch (IllegalArgumentException ignored) {
            }
        }

        Role.RoleBuilder builder = Role.builder()
                .id(roleDto.getId())
                .roleName(roleDto.getRoleName())
                .roleType(roleType);

        if (roleDto.getPermissions() != null) {
            Set<Permission> perms = new HashSet<>();
            for (String p : roleDto.getPermissions()) {
                try {
                    perms.add(Permission.valueOf(p));
                } catch (IllegalArgumentException ignored) {
                    // ignore unknown permission strings
                }
            }
            builder.permissions(perms);
        }

        return builder.build();
    }
}