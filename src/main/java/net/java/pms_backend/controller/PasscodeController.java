package net.java.pms_backend.repository;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.PasscodeDto;
import net.java.pms_backend.service.PasscodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/passcodes")
public class PasscodeController {

    private final PasscodeService passcodeService;

    // CREATE / SAVE
    @PostMapping
    public ResponseEntity<PasscodeDto> createPasscode(@RequestBody PasscodeDto passcodeDto) {
        PasscodeDto savedPasscode = passcodeService.createPasscode(passcodeDto);
        return new ResponseEntity<>(savedPasscode, HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<PasscodeDto>> getAllPasscodes() {
        List<PasscodeDto> passcodes = passcodeService.getAllPasscodes();
        return ResponseEntity.ok(passcodes);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PasscodeDto> updatePasscode(@PathVariable("id") Long id,
                                                      @RequestBody PasscodeDto passcodeDto) {
        PasscodeDto updatedPasscode = passcodeService.updatePasscode(id, passcodeDto);
        return ResponseEntity.ok(updatedPasscode);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePasscode(@PathVariable("id") Long id) {
        passcodeService.deletePasscode(id);
        return ResponseEntity.ok("Passcode record deleted successfully.");
    }
}