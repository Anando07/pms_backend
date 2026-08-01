package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.PasscodeDto;
import net.java.pms_backend.entity.Passcode;
import net.java.pms_backend.entity.User;
import net.java.pms_backend.mapper.PasscodeMapper;
import net.java.pms_backend.repository.PasscodeRepository;
import net.java.pms_backend.repository.UserRepository;
import net.java.pms_backend.service.PasscodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PasscodeServiceImpl implements PasscodeService {

    private final PasscodeRepository passcodeRepository;
    private final UserRepository userRepository;

    @Override
    public PasscodeDto createPasscode(PasscodeDto passcodeDto) {
        User user = userRepository.findById(passcodeDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + passcodeDto.getUserId()));

        Passcode passcode = PasscodeMapper.mapToPasscode(passcodeDto);
        passcode.setUser(user);

        Passcode savedPasscode = passcodeRepository.save(passcode);
        return PasscodeMapper.mapToPasscodeDto(savedPasscode);
    }
}