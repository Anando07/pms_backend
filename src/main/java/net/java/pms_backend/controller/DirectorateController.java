package net.java.pms_backend.controller;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.DirectorateDto;
import net.java.pms_backend.service.DirectorateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/directorates")
public class DirectorateController {

    private final DirectorateService directorateService;

    @PostMapping
    public ResponseEntity<DirectorateDto> createDirectorate(@RequestBody DirectorateDto directorateDto) {
        DirectorateDto saved = directorateService.createDirectorate(directorateDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DirectorateDto>> getAllDirectorates() {
        return ResponseEntity.ok(directorateService.getAllDirectorates());
    }

    @GetMapping("/ministry/{ministryId}")
    public ResponseEntity<List<DirectorateDto>> getDirectoratesByMinistry(@PathVariable Long ministryId) {
        return ResponseEntity.ok(directorateService.getDirectoratesByMinistry(ministryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorateDto> getDirectorateById(@PathVariable Long id) {
        return ResponseEntity.ok(directorateService.getDirectorateById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DirectorateDto> updateDirectorate(
            @PathVariable Long id,
            @RequestBody DirectorateDto directorateDto) {
        return ResponseEntity.ok(directorateService.updateDirectorate(id, directorateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirectorate(@PathVariable Long id) {
        directorateService.deleteDirectorate(id);
        return ResponseEntity.noContent().build();
    }
}
