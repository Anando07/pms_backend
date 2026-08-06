package net.java.pms_backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.PhysicalProgressDto;
import net.java.pms_backend.dto.ProjectWorkParameterDto;
import net.java.pms_backend.service.PhysicalProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/physical-progress")
@AllArgsConstructor
public class PhysicalProgressController {

    private final PhysicalProgressService physicalProgressService;

    // Stage 1: Target Parameters Config
    @PostMapping("/project/{projectId}/parameters")
    public ResponseEntity<List<ProjectWorkParameterDto>> saveProjectParameters(
            @PathVariable Long projectId,
            @Valid @RequestBody List<ProjectWorkParameterDto> parameters) {
        return new ResponseEntity<>(physicalProgressService.saveProjectWorkParameters(projectId, parameters), HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}/parameters")
    public ResponseEntity<List<ProjectWorkParameterDto>> getProjectParameters(@PathVariable Long projectId) {
        return ResponseEntity.ok(physicalProgressService.getProjectWorkParameters(projectId));
    }

    @PutMapping("/project/parameters/{parameterId}")
    public ResponseEntity<ProjectWorkParameterDto> updateProjectParameter(
            @PathVariable Long parameterId,
            @Valid @RequestBody ProjectWorkParameterDto dto) {
        return ResponseEntity.ok(physicalProgressService.updateProjectWorkParameter(parameterId, dto));
    }

    @DeleteMapping("/project/parameters/{parameterId}")
    public ResponseEntity<String> deleteProjectParameter(@PathVariable Long parameterId) {
        physicalProgressService.deleteProjectWorkParameter(parameterId);
        return ResponseEntity.ok("Project work parameter deleted successfully.");
    }

    // Stage 2: Progress Logging CRUD
    @PostMapping
    public ResponseEntity<PhysicalProgressDto> createProgress(@Valid @RequestBody PhysicalProgressDto dto) {
        return new ResponseEntity<>(physicalProgressService.createProgress(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhysicalProgressDto> getProgressById(@PathVariable Long id) {
        return ResponseEntity.ok(physicalProgressService.getProgressById(id));
    }

    @GetMapping
    public ResponseEntity<List<PhysicalProgressDto>> getAllProgress() {
        return ResponseEntity.ok(physicalProgressService.getAllProgress());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<PhysicalProgressDto>> getProgressByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(physicalProgressService.getProgressByProjectId(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhysicalProgressDto> updateProgress(@PathVariable Long id, @Valid @RequestBody PhysicalProgressDto dto) {
        return ResponseEntity.ok(physicalProgressService.updateProgress(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProgress(@PathVariable Long id) {
        physicalProgressService.deleteProgress(id);
        return ResponseEntity.ok("Physical progress record deleted successfully.");
    }
}