package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.entity.Permission;
import net.java.pms_backend.entity.Role;
import net.java.pms_backend.entity.User;
import net.java.pms_backend.repository.UserRepository;
import net.java.pms_backend.service.AccessService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class AccessServiceImpl implements AccessService {

    private final UserRepository userRepository;

    @Override
    public boolean userHasPermission(Long userId, Permission permission) {
        User user = userRepository.findById(userId).orElse(null);
//        if (user == null) return false;
        Role role = user.getRole();
        if (role == null) return false;
        Set<Permission> perms = role.getPermissions();
        return perms != null && perms.contains(permission);
    }
}

