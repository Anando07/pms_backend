package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.UserMinistryAccessDto;
import net.java.pms_backend.entity.AccessLevel;
import net.java.pms_backend.entity.UserMinistryAccess;

@SuppressWarnings("unused")
public class UserMinistryAccessMapper {

    private UserMinistryAccessMapper() {}

    public static UserMinistryAccessDto mapToDto(UserMinistryAccess entity) {
        if (entity == null) return null;

        return UserMinistryAccessDto.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .ministryId(entity.getMinistry() != null ? entity.getMinistry().getId() : null)
                .accessLevel(entity.getAccessLevel() != null ? entity.getAccessLevel().name() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static UserMinistryAccess mapToEntity(UserMinistryAccessDto dto) {
        if (dto == null) return null;

        AccessLevel level = null;
        if (dto.getAccessLevel() != null) {
            try {
                level = AccessLevel.valueOf(dto.getAccessLevel());
            } catch (IllegalArgumentException ignored) {
            }
        }

        return UserMinistryAccess.builder()
                .id(dto.getId())
                .accessLevel(level)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

