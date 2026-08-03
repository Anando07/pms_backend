package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.RoleAccessHierarchyDto;
import net.java.pms_backend.entity.AccessLevel;
import net.java.pms_backend.entity.RoleAccessHierarchy;
import net.java.pms_backend.entity.RoleType;

@SuppressWarnings("unused")
public class RoleAccessHierarchyMapper {

    private RoleAccessHierarchyMapper() {}

    public static RoleAccessHierarchyDto mapToDto(RoleAccessHierarchy entity) {
        if (entity == null) return null;

        return RoleAccessHierarchyDto.builder()
                .id(entity.getId())
                .roleType(entity.getRoleType() != null ? entity.getRoleType().name() : null)
                .ministryAccessLevel(entity.getMinistryAccessLevel() != null ? entity.getMinistryAccessLevel().name() : null)
                .projectAccessLevel(entity.getProjectAccessLevel() != null ? entity.getProjectAccessLevel().name() : null)
                .canAccessAllministries(entity.getCanAccessAllministries())
                .canAccessAllProjects(entity.getCanAccessAllProjects())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static RoleAccessHierarchy mapToEntity(RoleAccessHierarchyDto dto) {
        if (dto == null) return null;

        RoleType roleType = null;
        if (dto.getRoleType() != null) {
            try {
                roleType = RoleType.valueOf(dto.getRoleType());
            } catch (IllegalArgumentException ignored) {
            }
        }

        AccessLevel ministryLevel = null;
        if (dto.getMinistryAccessLevel() != null) {
            try {
                ministryLevel = AccessLevel.valueOf(dto.getMinistryAccessLevel());
            } catch (IllegalArgumentException ignored) {
            }
        }

        AccessLevel projectLevel = null;
        if (dto.getProjectAccessLevel() != null) {
            try {
                projectLevel = AccessLevel.valueOf(dto.getProjectAccessLevel());
            } catch (IllegalArgumentException ignored) {
            }
        }

        return RoleAccessHierarchy.builder()
                .id(dto.getId())
                .roleType(roleType)
                .ministryAccessLevel(ministryLevel)
                .projectAccessLevel(projectLevel)
                .canAccessAllministries(dto.getCanAccessAllministries())
                .canAccessAllProjects(dto.getCanAccessAllProjects())
                .description(dto.getDescription())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

