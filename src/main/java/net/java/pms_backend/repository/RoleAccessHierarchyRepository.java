package net.java.pms_backend.repository;

import net.java.pms_backend.entity.RoleAccessHierarchy;
import net.java.pms_backend.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleAccessHierarchyRepository extends JpaRepository<RoleAccessHierarchy, Long> {
    Optional<RoleAccessHierarchy> findByRoleType(RoleType roleType);
}

