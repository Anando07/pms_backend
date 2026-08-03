package net.java.pms_backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleAccessHierarchyDto {
    private Long id;
    private String roleType;
    private String ministryAccessLevel;
    private String projectAccessLevel;
    private Boolean canAccessAllministries;
    private Boolean canAccessAllProjects;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

