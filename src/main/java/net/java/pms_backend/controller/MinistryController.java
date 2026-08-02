package net.java.pms_backend.controller;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.MinistryDto;
import net.java.pms_backend.service.MinistryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/ministries")
public class MinistryController {

    private final MinistryService ministryService;

    @PostMapping
    public ResponseEntity<MinistryDto> createMinistry(@RequestBody MinistryDto ministryDto) {
        MinistryDto savedMinistry = ministryService.createMinistry(ministryDto);
        return new ResponseEntity<>(savedMinistry, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MinistryDto>> getAllMinistries() {
        List<MinistryDto> ministries = ministryService.getAllMinistries();
        return ResponseEntity.ok(ministries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MinistryDto> getMinistryById(@PathVariable Long id) {
        MinistryDto ministry = ministryService.getMinistryById(id);
        return ResponseEntity.ok(ministry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MinistryDto> updateMinistry(
            @PathVariable Long id,
            @RequestBody MinistryDto ministryDto) {
        MinistryDto updatedMinistry = ministryService.updateMinistry(id, ministryDto);
        return ResponseEntity.ok(updatedMinistry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMinistry(@PathVariable Long id) {
        ministryService.deleteMinistry(id);
        return ResponseEntity.noContent().build();
    }
}