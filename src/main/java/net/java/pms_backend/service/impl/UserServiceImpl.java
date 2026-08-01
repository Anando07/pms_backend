package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.UserDto;
import net.java.pms_backend.entity.Role;
import net.java.pms_backend.entity.User;
import net.java.pms_backend.mapper.UserMapper;
import net.java.pms_backend.repository.RoleRepository; // <--- ADD THIS
import net.java.pms_backend.repository.UserRepository;
import net.java.pms_backend.service.UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);

        // Fetch managed Role entity from DB if roleId is provided
        if (userDto.getRoleId() != null) {
            Role role = roleRepository.findById(userDto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + userDto.getRoleId()));
            user.setRole(role);
        }

        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }
}