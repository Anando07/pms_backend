package net.java.pms_backend.service;

import net.java.pms_backend.entity.AccessLevel;

public interface AccessControlService {

    /**
     * Check if a user can access a ministry at the specified access level
     */
    boolean canAccessMinistry(Long userId, Long ministryId);

    /**
     * Check if a user can access a project at the specified access level
     */
    boolean canAccessProject(Long userId, Long projectId);

    /**
     * Check if a user has a specific access level for a ministry
     */
    boolean hasMinistryAccessLevel(Long userId, Long ministryId, AccessLevel accessLevel);

    /**
     * Check if a user has a specific access level for a project
     */
    boolean hasProjectAccessLevel(Long userId, Long projectId, AccessLevel accessLevel);

    /**
     * Get user's access level for a ministry
     */
    AccessLevel getUserMinistryAccessLevel(Long userId, Long ministryId);

    /**
     * Get user's access level for a project
     */
    AccessLevel getUserProjectAccessLevel(Long userId, Long projectId);

    /**
     * Grant or update ministry access for a user
     */
    void grantMinistryAccess(Long userId, Long ministryId, AccessLevel accessLevel);

    /**
     * Grant or update project access for a user
     */
    void grantProjectAccess(Long userId, Long projectId, AccessLevel accessLevel);

    /**
     * Revoke ministry access for a user
     */
    void revokeMinistryAccess(Long userId, Long ministryId);

    /**
     * Revoke project access for a user
     */
    void revokeProjectAccess(Long userId, Long projectId);
}

