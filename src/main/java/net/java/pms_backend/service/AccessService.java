package net.java.pms_backend.service;

import net.java.pms_backend.entity.Permission;

public interface AccessService {
    boolean userHasPermission(Long userId, Permission permission);
}

