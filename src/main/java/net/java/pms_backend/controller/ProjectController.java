package net.java.pms_backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.ProjectDto;
import net.java.pms_backend.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectDto projectDto) {
        ProjectDto savedProject = projectService.createProject(projectDto);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        ProjectDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectDto projectDto) {
        ProjectDto updatedProject = projectService.updateProject(id, projectDto);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
