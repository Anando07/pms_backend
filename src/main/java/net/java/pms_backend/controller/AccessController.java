package net.java.pms_backend.controller;

import lombok.AllArgsConstructor;
import net.java.pms_backend.entity.Permission;
import net.java.pms_backend.service.AccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/access")
@AllArgsConstructor
public class AccessController {

    private final AccessService accessService;

    @GetMapping("/users/{userId}/has/{permission}")
    public ResponseEntity<Boolean> userHasPermission(@PathVariable("userId") Long userId,
                                                     @PathVariable("permission") String permission) {
        try {
            Permission p = Permission.valueOf(permission);
            boolean has = accessService.userHasPermission(userId, p);
            return ResponseEntity.ok(has);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}

