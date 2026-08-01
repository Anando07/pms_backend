package net.java.pms_backend.service;

import net.java.pms_backend.dto.PasscodeDto;
import java.util.List;

public interface PasscodeService {
    PasscodeDto createPasscode(PasscodeDto passcodeDto);
    List<PasscodeDto> getAllPasscodes();
    PasscodeDto updatePasscode(Long id, PasscodeDto passcodeDto);
    void deletePasscode(Long id);
}