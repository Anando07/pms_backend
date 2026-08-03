package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.UserProjectAccessDto;
import net.java.pms_backend.entity.AccessLevel;
import net.java.pms_backend.entity.UserProjectAccess;

@SuppressWarnings("unused")
public class UserProjectAccessMapper {

    private UserProjectAccessMapper() {}

    public static UserProjectAccessDto mapToDto(UserProjectAccess entity) {
        if (entity == null) return null;

        return UserProjectAccessDto.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .projectId(entity.getProject() != null ? entity.getProject().getId() : null)
                .accessLevel(entity.getAccessLevel() != null ? entity.getAccessLevel().name() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static UserProjectAccess mapToEntity(UserProjectAccessDto dto) {
        if (dto == null) return null;

        AccessLevel level = null;
        if (dto.getAccessLevel() != null) {
            try {
                level = AccessLevel.valueOf(dto.getAccessLevel());
            } catch (IllegalArgumentException ignored) {
            }
        }

        return UserProjectAccess.builder()
                .id(dto.getId())
                .accessLevel(level)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

