package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.UserDto;
import net.java.pms_backend.entity.User;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class UserMapper {

    private UserMapper() {}

    public static UserDto mapToUserDto(User user) {
        if (user == null) return null;

        Long roleId = (user.getRole() != null) ? user.getRole().getId() : null;
        String roleName = (user.getRole() != null) ? user.getRole().getRoleName() : null;

        String avatar = null;
        if (user.getProfileImages() != null && !user.getProfileImages().isEmpty()) {
            avatar = user.getProfileImages().get(0);
        }

        Long ministryId = (user.getMinistry() != null) ? user.getMinistry().getId() : null;
        String ministryName = (user.getMinistry() != null) ? user.getMinistry().getMinName() : null;

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .designation(user.getDesignation())
                .officeName(user.getOfficeName())
                .email(user.getEmail())
                .number(user.getNumber())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roleId(roleId)
                .roleName(roleName)
                .avatar(avatar)
                .ministryId(ministryId)
                .ministryName(ministryName)
                .build();
    }

    public static User mapToUser(UserDto userDto) {
        if (userDto == null) return null;

        List<String> images = new ArrayList<>();
        if (userDto.getAvatar() != null && !userDto.getAvatar().isBlank()) {
            images.add(userDto.getAvatar());
        }

        User user = User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .designation(userDto.getDesignation())
                .officeName(userDto.getOfficeName())
                .email(userDto.getEmail())
                .number(userDto.getNumber())
                .active(userDto.getActive() == null || userDto.getActive())
                .createdAt(userDto.getCreatedAt())
                .updatedAt(userDto.getUpdatedAt())
                .profileImages(images)
                .build();

        // Role mapping is handled in UserServiceImpl (needs a DB lookup for the managed entity).

        return user;
    }
}
