package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.PasscodeDto;
import net.java.pms_backend.entity.Passcode;
import net.java.pms_backend.entity.User;

public class PasscodeMapper {

    private PasscodeMapper() {}

    public static PasscodeDto mapToPasscodeDto(Passcode passcode) {
        if (passcode == null) return null;

        User user = passcode.getUser();

        return PasscodeDto.builder()
                .id(passcode.getId())
                .passcode(passcode.getPasscode())
                .active(passcode.getActive())
                .expiresAt(passcode.getExpiresAt())
                .createdAt(passcode.getCreatedAt())
                .updatedAt(passcode.getUpdatedAt())
                .userId(user != null ? user.getId() : null)
                .userName(user != null ? user.getName() : null)
                .userEmail(user != null ? user.getEmail() : null)
                .build();
    }

    public static Passcode mapToPasscode(PasscodeDto passcodeDto) {
        if (passcodeDto == null) return null;

        return Passcode.builder()
                .id(passcodeDto.getId())
                .passcode(passcodeDto.getPasscode())
                .active(passcodeDto.getActive() == null || passcodeDto.getActive())
                .expiresAt(passcodeDto.getExpiresAt())
                .createdAt(passcodeDto.getCreatedAt())
                .updatedAt(passcodeDto.getUpdatedAt())
                .build();
    }
}