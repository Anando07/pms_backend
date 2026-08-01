package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.UserDto;
import net.java.pms_backend.entity.Passcode;
import net.java.pms_backend.entity.Role;
import net.java.pms_backend.entity.User;

@SuppressWarnings("unused")
public class UserMapper {

    private UserMapper() {}

    public static UserDto mapToUserDto(User user) {
        if (user == null) return null;

        Long roleId = (user.getRole() != null) ? user.getRole().getId() : null;
        String roleName = (user.getRole() != null) ? user.getRole().getRoleName() : null;

        Passcode passcode = user.getPasscode();

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .designation(user.getDesignation())
                .officeName(user.getOfficeName())
                .email(user.getEmail())
                .number(user.getNumber())
                .minDiv(user.getMinDiv())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roleId(roleId)
                .roleName(roleName)
                .passcode(passcode != null ? passcode.getPasscode() : null)
                .passcodeActive(passcode != null ? passcode.getActive() : null)
                .passcodeExpiresAt(passcode != null ? passcode.getExpiresAt() : null)
                .build();
    }

    public static User mapToUser(UserDto userDto) {
        if (userDto == null) return null;

        User user = User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .designation(userDto.getDesignation())
                .officeName(userDto.getOfficeName())
                .email(userDto.getEmail())
                .number(userDto.getNumber())
                .minDiv(userDto.getMinDiv())
                .active(userDto.getActive() == null || userDto.getActive())
                .createdAt(userDto.getCreatedAt())
                .updatedAt(userDto.getUpdatedAt())
                .build();

        // Role mapping is handled safely in UserServiceImpl

        // Map Passcode
        if (userDto.getPasscode() != null) {
            Passcode passcode = Passcode.builder()
                    .passcode(userDto.getPasscode())
                    .active(userDto.getPasscodeActive() == null || userDto.getPasscodeActive())
                    .expiresAt(userDto.getPasscodeExpiresAt())
                    .user(user)
                    .build();
            user.setPasscode(passcode);
        }

        return user;
    }
}