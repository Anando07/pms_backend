package net.java.pms_backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.DirectorDto;
import net.java.pms_backend.service.DirectorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/directors")
public class DirectorController {

    private final DirectorService directorService;

    // CREATE Director
    @PostMapping
    public ResponseEntity<DirectorDto> createDirector(@Valid @RequestBody DirectorDto directorDto) {
        DirectorDto savedDirector = directorService.createDirector(directorDto);
        return new ResponseEntity<>(savedDirector, HttpStatus.CREATED);
    }

    // GET All Directors
    @GetMapping
    public ResponseEntity<List<DirectorDto>> getAllDirectors() {
        return ResponseEntity.ok(directorService.getAllDirectors());
    }

    // GET Director by ID
    @GetMapping("/{id}")
    public ResponseEntity<DirectorDto> getDirectorById(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.getDirectorById(id));
    }

    // GET Directors by Project ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<DirectorDto>> getDirectorsByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(directorService.getDirectorsByProjectId(projectId));
    }

    // UPDATE Director
    @PutMapping("/{id}")
    public ResponseEntity<DirectorDto> updateDirector(
            @PathVariable Long id,
            @Valid @RequestBody DirectorDto directorDto) {
        return ResponseEntity.ok(directorService.updateDirector(id, directorDto));
    }

    // DELETE Director
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirector(@PathVariable Long id) {
        directorService.deleteDirector(id);
        return ResponseEntity.noContent().build();
    }
}