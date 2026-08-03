package net.java.pms_backend.repository;

import net.java.pms_backend.entity.UserMinistryAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMinistryAccessRepository extends JpaRepository<UserMinistryAccess, Long> {
    List<UserMinistryAccess> findByUserId(Long userId);
    Optional<UserMinistryAccess> findByUserIdAndMinistryId(Long userId, Long ministryId);
    List<UserMinistryAccess> findByMinistryId(Long ministryId);
}

