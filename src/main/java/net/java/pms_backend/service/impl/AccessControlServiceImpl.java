package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.entity.*;
import net.java.pms_backend.repository.UserMinistryAccessRepository;
import net.java.pms_backend.repository.UserProjectAccessRepository;
import net.java.pms_backend.repository.UserRepository;
import net.java.pms_backend.repository.RoleAccessHierarchyRepository;
import net.java.pms_backend.service.AccessControlService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccessControlServiceImpl implements AccessControlService {

    private final UserRepository userRepository;
    private final UserMinistryAccessRepository userMinistryAccessRepository;
    private final UserProjectAccessRepository userProjectAccessRepository;
    @SuppressWarnings("unused")
    private final RoleAccessHierarchyRepository roleAccessHierarchyRepository;

    @Override
    public boolean canAccessMinistry(Long userId, Long ministryId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Super Admin can access all
        if (isSuperAdmin(user)) return true;

        // Check explicit ministry access
        return userMinistryAccessRepository.findByUserIdAndMinistryId(userId, ministryId).isPresent();
    }

    @Override
    public boolean canAccessProject(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Super Admin can access all
        if (isSuperAdmin(user)) return true;

        // Check explicit project access
        return userProjectAccessRepository.findByUserIdAndProjectId(userId, projectId).isPresent();
    }

    @Override
    public boolean hasMinistryAccessLevel(Long userId, Long ministryId, AccessLevel requiredLevel) {
        AccessLevel userLevel = getUserMinistryAccessLevel(userId, ministryId);
        if (userLevel == null || userLevel == AccessLevel.NONE) return false;

        return canPerformAction(userLevel, requiredLevel);
    }

    @Override
    public boolean hasProjectAccessLevel(Long userId, Long projectId, AccessLevel requiredLevel) {
        AccessLevel userLevel = getUserProjectAccessLevel(userId, projectId);
        if (userLevel == null || userLevel == AccessLevel.NONE) return false;

        return canPerformAction(userLevel, requiredLevel);
    }

    @Override
    public AccessLevel getUserMinistryAccessLevel(Long userId, Long ministryId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return AccessLevel.NONE;

        // Super Admin has full access
        if (isSuperAdmin(user)) return AccessLevel.FULL;

        Optional<UserMinistryAccess> access = userMinistryAccessRepository.findByUserIdAndMinistryId(userId, ministryId);
        return access.map(UserMinistryAccess::getAccessLevel).orElse(AccessLevel.NONE);
    }

    @Override
    public AccessLevel getUserProjectAccessLevel(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return AccessLevel.NONE;

        // Super Admin has full access
        if (isSuperAdmin(user)) return AccessLevel.FULL;

        Optional<UserProjectAccess> access = userProjectAccessRepository.findByUserIdAndProjectId(userId, projectId);
        return access.map(UserProjectAccess::getAccessLevel).orElse(AccessLevel.NONE);
    }

    @Override
    public void grantMinistryAccess(Long userId, Long ministryId, AccessLevel accessLevel) {
        Optional<UserMinistryAccess> existing = userMinistryAccessRepository.findByUserIdAndMinistryId(userId, ministryId);

        if (existing.isPresent()) {
            UserMinistryAccess access = existing.get();
            access.setAccessLevel(accessLevel);
            userMinistryAccessRepository.save(access);
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            Ministry ministry = new Ministry();
            ministry.setId(ministryId);

            UserMinistryAccess access = UserMinistryAccess.builder()
                    .user(user)
                    .ministry(ministry)
                    .accessLevel(accessLevel)
                    .build();
            userMinistryAccessRepository.save(access);
        }
    }

    @Override
    public void grantProjectAccess(Long userId, Long projectId, AccessLevel accessLevel) {
        Optional<UserProjectAccess> existing = userProjectAccessRepository.findByUserIdAndProjectId(userId, projectId);

        if (existing.isPresent()) {
            UserProjectAccess access = existing.get();
            access.setAccessLevel(accessLevel);
            userProjectAccessRepository.save(access);
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            Project project = new Project();
            project.setId(projectId);

            UserProjectAccess access = UserProjectAccess.builder()
                    .user(user)
                    .project(project)
                    .accessLevel(accessLevel)
                    .build();
            userProjectAccessRepository.save(access);
        }
    }

    @Override
    public void revokeMinistryAccess(Long userId, Long ministryId) {
        Optional<UserMinistryAccess> access = userMinistryAccessRepository.findByUserIdAndMinistryId(userId, ministryId);
        access.ifPresent(userMinistryAccessRepository::delete);
    }

    @Override
    public void revokeProjectAccess(Long userId, Long projectId) {
        Optional<UserProjectAccess> access = userProjectAccessRepository.findByUserIdAndProjectId(userId, projectId);
        access.ifPresent(userProjectAccessRepository::delete);
    }

    /**
     * Check if user is Super Admin
     */
    private boolean isSuperAdmin(User user) {
        if (user.getRole() == null) return false;
        return user.getRole().getRoleType() == RoleType.SUPER_ADMIN;
    }

    /**
     * Check if the user's access level allows the requested action
     */
    private boolean canPerformAction(AccessLevel userLevel, AccessLevel requiredLevel) {
        if (userLevel == AccessLevel.FULL) return true;
        return userLevel.equals(requiredLevel) || isHigherLevel(userLevel, requiredLevel);
    }

    /**
     * Hierarchy: FULL > PROJECT > MINISTRY > CREATE_EDIT > DATA_ENTRY > VIEW_ONLY > NONE
     */
    private boolean isHigherLevel(AccessLevel userLevel, AccessLevel requiredLevel) {
        int userRank = getAccessRank(userLevel);
        int requiredRank = getAccessRank(requiredLevel);
        return userRank > requiredRank;
    }

    private int getAccessRank(AccessLevel level) {
        return switch (level) {
            case FULL -> 7;
            case PROJECT -> 6;
            case MINISTRY -> 5;
            case CREATE_EDIT -> 4;
            case DATA_ENTRY -> 3;
            case VIEW_ONLY -> 2;
            default -> 0;
        };
    }
}


