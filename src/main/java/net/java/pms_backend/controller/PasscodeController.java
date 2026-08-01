package net.java.pms_backend.controller;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.PasscodeDto;
import net.java.pms_backend.service.PasscodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/passcodes")
public class PasscodeController {
    private PasscodeService passcodeService;
    //Build Add User REST API
    @PostMapping
    public ResponseEntity<PasscodeDto> createPasscode (@RequestBody PasscodeDto passcodeDto){
        PasscodeDto savedPassword = passcodeService.createPasscode(passcodeDto);
        return new ResponseEntity<>(savedPassword, HttpStatus.CREATED);
    }


}
