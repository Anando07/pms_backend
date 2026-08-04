package net.java.pms_backend.controller;

import lombok.RequiredArgsConstructor;
import net.java.pms_backend.dto.UserDto;
import net.java.pms_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboardResponse = new HashMap<>();

        // Add summary values and metrics directly from your entities/services
        dashboardResponse.put("totalProjects", 200);
        dashboardResponse.put("runningProjects", 100);
        dashboardResponse.put("completedProjects", 30);
        dashboardResponse.put("delayedProjects", 20);
        dashboardResponse.put("cancelledProjects", 5);

        return ResponseEntity.ok(dashboardResponse);
    }
}