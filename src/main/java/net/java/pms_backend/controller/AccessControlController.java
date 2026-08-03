package net.java.pms_backend.controller;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.UserMinistryAccessDto;
import net.java.pms_backend.dto.UserProjectAccessDto;
import net.java.pms_backend.entity.AccessLevel;
import net.java.pms_backend.entity.UserMinistryAccess;
import net.java.pms_backend.entity.UserProjectAccess;
import net.java.pms_backend.mapper.UserMinistryAccessMapper;
import net.java.pms_backend.mapper.UserProjectAccessMapper;
import net.java.pms_backend.repository.UserMinistryAccessRepository;
import net.java.pms_backend.repository.UserProjectAccessRepository;
import net.java.pms_backend.service.AccessControlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/access-control")
@AllArgsConstructor
public class AccessControlController {

    private final AccessControlService accessControlService;
    private final UserMinistryAccessRepository userMinistryAccessRepository;
    private final UserProjectAccessRepository userProjectAccessRepository;

    // ==================== User Ministry Access ====================

    /**
     * Grant or update ministry access for a user
     */
    @PostMapping("/users/{userId}/ministries/{ministryId}")
    public ResponseEntity<UserMinistryAccessDto> grantMinistryAccess(
            @PathVariable("userId") Long userId,
            @PathVariable("ministryId") Long ministryId,
            @RequestParam String accessLevel) {
        try {
            AccessLevel level = AccessLevel.valueOf(accessLevel);
            accessControlService.grantMinistryAccess(userId, ministryId, level);
            UserMinistryAccess access = userMinistryAccessRepository.findByUserIdAndMinistryId(userId, ministryId)
                    .orElseThrow(() -> new RuntimeException("Access not found"));
            return new ResponseEntity<>(UserMinistryAccessMapper.mapToDto(access), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Check if user can access ministry
     */
    @GetMapping("/users/{userId}/ministries/{ministryId}/can-access")
    public ResponseEntity<Boolean> canAccessMinistry(
            @PathVariable("userId") Long userId,
            @PathVariable("ministryId") Long ministryId) {
        boolean canAccess = accessControlService.canAccessMinistry(userId, ministryId);
        return ResponseEntity.ok(canAccess);
    }

    /**
     * Get user's access level for ministry
     */
    @GetMapping("/users/{userId}/ministries/{ministryId}/access-level")
    public ResponseEntity<String> getMinistryAccessLevel(
            @PathVariable("userId") Long userId,
            @PathVariable("ministryId") Long ministryId) {
        AccessLevel level = accessControlService.getUserMinistryAccessLevel(userId, ministryId);
        return ResponseEntity.ok(level.name());
    }

    /**
     * Get all ministries accessible to a user
     */
    @GetMapping("/users/{userId}/ministries")
    public ResponseEntity<List<UserMinistryAccessDto>> getUserMinistryAccess(
            @PathVariable("userId") Long userId) {
        List<UserMinistryAccessDto> access = userMinistryAccessRepository.findByUserId(userId)
                .stream()
                .map(UserMinistryAccessMapper::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(access);
    }

    /**
     * Revoke ministry access for a user
     */
    @DeleteMapping("/users/{userId}/ministries/{ministryId}")
    public ResponseEntity<String> revokeMinistryAccess(
            @PathVariable("userId") Long userId,
            @PathVariable("ministryId") Long ministryId) {
        accessControlService.revokeMinistryAccess(userId, ministryId);
        return ResponseEntity.ok("Ministry access revoked successfully");
    }

    // ==================== User Project Access ====================

    /**
     * Grant or update project access for a user
     */
    @PostMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<UserProjectAccessDto> grantProjectAccess(
            @PathVariable("userId") Long userId,
            @PathVariable("projectId") Long projectId,
            @RequestParam String accessLevel) {
        try {
            AccessLevel level = AccessLevel.valueOf(accessLevel);
            accessControlService.grantProjectAccess(userId, projectId, level);
            UserProjectAccess access = userProjectAccessRepository.findByUserIdAndProjectId(userId, projectId)
                    .orElseThrow(() -> new RuntimeException("Access not found"));
            return new ResponseEntity<>(UserProjectAccessMapper.mapToDto(access), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Check if user can access project
     */
    @GetMapping("/users/{userId}/projects/{projectId}/can-access")
    public ResponseEntity<Boolean> canAccessProject(
            @PathVariable("userId") Long userId,
            @PathVariable("projectId") Long projectId) {
        boolean canAccess = accessControlService.canAccessProject(userId, projectId);
        return ResponseEntity.ok(canAccess);
    }

    /**
     * Get user's access level for project
     */
    @GetMapping("/users/{userId}/projects/{projectId}/access-level")
    public ResponseEntity<String> getProjectAccessLevel(
            @PathVariable("userId") Long userId,
            @PathVariable("projectId") Long projectId) {
        AccessLevel level = accessControlService.getUserProjectAccessLevel(userId, projectId);
        return ResponseEntity.ok(level.name());
    }

    /**
     * Get all projects accessible to a user
     */
    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<List<UserProjectAccessDto>> getUserProjectAccess(
            @PathVariable("userId") Long userId) {
        List<UserProjectAccessDto> access = userProjectAccessRepository.findByUserId(userId)
                .stream()
                .map(UserProjectAccessMapper::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(access);
    }

    /**
     * Revoke project access for a user
     */
    @DeleteMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<String> revokeProjectAccess(
            @PathVariable("userId") Long userId,
            @PathVariable("projectId") Long projectId) {
        accessControlService.revokeProjectAccess(userId, projectId);
        return ResponseEntity.ok("Project access revoked successfully");
    }
}

