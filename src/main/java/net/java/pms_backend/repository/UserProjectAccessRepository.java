package net.java.pms_backend.repository;

import net.java.pms_backend.entity.UserProjectAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProjectAccessRepository extends JpaRepository<UserProjectAccess, Long> {
    List<UserProjectAccess> findByUserId(Long userId);
    Optional<UserProjectAccess> findByUserIdAndProjectId(Long userId, Long projectId);
    List<UserProjectAccess> findByProjectId(Long projectId);
}

