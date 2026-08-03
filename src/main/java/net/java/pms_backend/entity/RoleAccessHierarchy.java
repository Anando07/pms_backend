package net.java.pms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role_access_hierarchy")
public class RoleAccessHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ministry_access_level", nullable = false)
    private AccessLevel ministryAccessLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_access_level", nullable = false)
    private AccessLevel projectAccessLevel;

    @Column(name = "can_access_all_ministries", nullable = false)
    private Boolean canAccessAllministries = false;

    @Column(name = "can_access_all_projects", nullable = false)
    private Boolean canAccessAllProjects = false;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

