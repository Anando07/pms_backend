package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.PasscodeDto;
import net.java.pms_backend.entity.Passcode;
import net.java.pms_backend.entity.User;
import net.java.pms_backend.mapper.PasscodeMapper;
import net.java.pms_backend.repository.PasscodeRepository;
import net.java.pms_backend.repository.UserRepository;
import net.java.pms_backend.service.PasscodeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PasscodeServiceImpl implements PasscodeService {

    private final PasscodeRepository passcodeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // SAVE / CREATE (Or update if user already has a passcode)
    @Override
    @Transactional
    public PasscodeDto createPasscode(PasscodeDto passcodeDto) {
        User user = userRepository.findById(passcodeDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + passcodeDto.getUserId()));

        // Check if user already has a passcode; overwrite if present
        Passcode passcode = passcodeRepository.findByUserId(user.getId())
                .orElseGet(() -> PasscodeMapper.mapToPasscode(passcodeDto));

        passcode.setUser(user);
        passcode.setPasscode(passwordEncoder.encode(passcodeDto.getPasscode()));
        passcode.setActive(passcodeDto.getActive() != null ? passcodeDto.getActive() : true);

        // Set expiration date from form if provided, otherwise default to 3 months from now
        if (passcodeDto.getExpiresAt() != null) {
            passcode.setExpiresAt(passcodeDto.getExpiresAt());
        } else {
            passcode.setExpiresAt(LocalDate.now().plusMonths(3).atStartOfDay());
        }

        Passcode savedPasscode = passcodeRepository.save(passcode);
        return PasscodeMapper.mapToPasscodeDto(savedPasscode);
    }

    // GET ALL
    @Override
    public List<PasscodeDto> getAllPasscodes() {
        List<Passcode> passcodes = passcodeRepository.findAll();
        return passcodes.stream()
                .map(PasscodeMapper::mapToPasscodeDto)
                .collect(Collectors.toList());
    }

    // UPDATE BY ID
    @Override
    @Transactional
    public PasscodeDto updatePasscode(Long id, PasscodeDto passcodeDto) {
        Passcode existingPasscode = passcodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passcode record not found with ID: " + id));

        if (passcodeDto.getPasscode() != null && !passcodeDto.getPasscode().isBlank()) {
            existingPasscode.setPasscode(passwordEncoder.encode(passcodeDto.getPasscode()));
        }

        if (passcodeDto.getActive() != null) {
            existingPasscode.setActive(passcodeDto.getActive());
        }

        // Set expiration date from form date if present, otherwise default to 3 months from now
        if (passcodeDto.getExpiresAt() != null) {
            existingPasscode.setExpiresAt(passcodeDto.getExpiresAt());
        } else {
            existingPasscode.setExpiresAt(LocalDate.now().plusMonths(3).atStartOfDay());
        }

        Passcode updatedPasscode = passcodeRepository.save(existingPasscode);
        return PasscodeMapper.mapToPasscodeDto(updatedPasscode);
    }

    // DELETE BY ID
    @Override
    @Transactional
    public void deletePasscode(Long id) {
        Passcode passcode = passcodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passcode record not found with ID: " + id));
        passcodeRepository.delete(passcode);
    }
}