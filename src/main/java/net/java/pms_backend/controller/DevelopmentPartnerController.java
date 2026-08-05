package net.java.pms_backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.DevelopmentPartnerDto;
import net.java.pms_backend.service.DevelopmentPartnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/devpartners") // <-- Matches http://localhost:8080/api/devpartners
public class DevelopmentPartnerController {

    private final DevelopmentPartnerService developmentPartnerService;

    @GetMapping
    public ResponseEntity<List<DevelopmentPartnerDto>> getAllDevelopmentPartners() {
        List<DevelopmentPartnerDto> partners = developmentPartnerService.getAllDevelopmentPartners();
        return ResponseEntity.ok(partners);
    }

    @PostMapping
    public ResponseEntity<DevelopmentPartnerDto> createDevelopmentPartner(
            @Valid @RequestBody DevelopmentPartnerDto developmentPartnerDto) {
        DevelopmentPartnerDto savedPartner = developmentPartnerService.createDevelopmentPartner(developmentPartnerDto);
        return new ResponseEntity<>(savedPartner, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevelopmentPartnerDto> getDevelopmentPartnerById(@PathVariable Long id) {
        DevelopmentPartnerDto partner = developmentPartnerService.getDevelopmentPartnerById(id);
        return ResponseEntity.ok(partner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DevelopmentPartnerDto> updateDevelopmentPartner(
            @PathVariable Long id,
            @Valid @RequestBody DevelopmentPartnerDto developmentPartnerDto) {
        DevelopmentPartnerDto updatedPartner = developmentPartnerService.updateDevelopmentPartner(id, developmentPartnerDto);
        return ResponseEntity.ok(updatedPartner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevelopmentPartner(@PathVariable Long id) {
        developmentPartnerService.deleteDevelopmentPartner(id);
        return ResponseEntity.noContent().build();
    }
}