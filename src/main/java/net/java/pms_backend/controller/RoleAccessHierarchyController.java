package net.java.pms_backend.controller;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.RoleAccessHierarchyDto;
import net.java.pms_backend.service.RoleAccessHierarchyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/role-access-hierarchy")
@AllArgsConstructor
public class RoleAccessHierarchyController {

    private final RoleAccessHierarchyService service;

    /**
     * Create a new role access hierarchy
     */
    @PostMapping
    public ResponseEntity<RoleAccessHierarchyDto> createHierarchy(@RequestBody RoleAccessHierarchyDto dto) {
        RoleAccessHierarchyDto saved = service.createHierarchy(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Get hierarchy by role type
     */
    @GetMapping("/role-type/{roleType}")
    public ResponseEntity<RoleAccessHierarchyDto> getHierarchyByRoleType(
            @PathVariable("roleType") String roleType) {
        RoleAccessHierarchyDto hierarchy = service.getHierarchyByRoleType(roleType);
        return ResponseEntity.ok(hierarchy);
    }

    /**
     * Get all hierarchies
     */
    @GetMapping
    public ResponseEntity<List<RoleAccessHierarchyDto>> getAllHierarchies() {
        List<RoleAccessHierarchyDto> hierarchies = service.getAllHierarchies();
        return ResponseEntity.ok(hierarchies);
    }

    /**
     * Update hierarchy
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleAccessHierarchyDto> updateHierarchy(
            @PathVariable("id") Long id,
            @RequestBody RoleAccessHierarchyDto dto) {
        RoleAccessHierarchyDto updated = service.updateHierarchy(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete hierarchy
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHierarchy(@PathVariable("id") Long id) {
        service.deleteHierarchy(id);
        return ResponseEntity.ok("Hierarchy deleted successfully");
    }
}

