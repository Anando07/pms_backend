package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.UserDto;
import net.java.pms_backend.entity.Role;
import net.java.pms_backend.entity.User;
import net.java.pms_backend.entity.Ministry;
import net.java.pms_backend.mapper.UserMapper;
import net.java.pms_backend.repository.RoleRepository;
import net.java.pms_backend.repository.UserRepository;
import net.java.pms_backend.repository.MinistryRepository;
import net.java.pms_backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MinistryRepository ministryRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        validateRequiredFields(userDto);

        // UserMapper.mapToUser copies userDto.getAvatar() into user.profileImages,
        // so the image is persisted on create instead of being silently dropped.
        User user = UserMapper.mapToUser(userDto);

        user.setRole(resolveRole(userDto.getRoleId(), true));
        user.setMinistry(resolveMinistry(userDto.getMinistryId(), false));

        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = findUserOrThrow(userId);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long userId, UserDto updatedUser) {
        validateRequiredFields(updatedUser);

        User user = findUserOrThrow(userId);

        user.setName(updatedUser.getName());
        user.setDesignation(updatedUser.getDesignation());
        user.setOfficeName(updatedUser.getOfficeName());
        user.setEmail(updatedUser.getEmail());
        user.setNumber(updatedUser.getNumber());

        if (updatedUser.getActive() != null) {
            user.setActive(updatedUser.getActive());
        }

        user.setRole(resolveRole(updatedUser.getRoleId(), false));

        // Only update ministry if the client explicitly provided a ministryId.
        // This preserves the existing ministry when the field is omitted from the payload.
        if (updatedUser.getMinistryId() != null) {
            user.setMinistry(resolveMinistry(updatedUser.getMinistryId(), false));
        }

        // Image is optional:
        // - avatar == null  -> field wasn't sent, leave the existing image untouched
        // - avatar == ""    -> user explicitly removed the image
        // - avatar == "..." -> new image, replaces the existing one
        if (updatedUser.getAvatar() != null) {
            List<String> imgs = new ArrayList<>();
            if (!updatedUser.getAvatar().isBlank()) {
                imgs.add(updatedUser.getAvatar());
            }
            user.setProfileImages(imgs);
        }

        User updatedUserObj = userRepository.save(user);
        return UserMapper.mapToUserDto(updatedUserObj);
    }

    @Override
    public void deleteUser(Long userId) {
        // findUserOrThrow ensures a clean 404-style error instead of a silent no-op
        // if the ID doesn't exist.
        findUserOrThrow(userId);
        userRepository.deleteById(userId);
    }

    // ---- helpers ----

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    private Role resolveRole(Long roleId, boolean required) {
        if (roleId == null) {
            if (required) {
                throw new IllegalArgumentException("Role is required.");
            }
            return null;
        }
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
    }

    private Ministry resolveMinistry(Long ministryId, boolean required) {
        if (ministryId == null) {
            if (required) {
                throw new IllegalArgumentException("Ministry is required.");
            }
            return null;
        }
        return ministryRepository.findById(ministryId)
                .orElseThrow(() -> new RuntimeException("Ministry not found with ID: " + ministryId));
    }

    /**
     * Every field except the image (avatar) is required.
     * Enforced here so bad requests fail with a clear message instead of a
     * raw DataIntegrityViolationException from the DB's NOT NULL constraints.
     */
    private void validateRequiredFields(UserDto dto) {
        List<String> missing = new ArrayList<>();

        if (isBlank(dto.getName())) missing.add("name");
        if (isBlank(dto.getDesignation())) missing.add("designation");
        if (isBlank(dto.getOfficeName())) missing.add("officeName");
        if (isBlank(dto.getEmail())) missing.add("email");
        if (isBlank(dto.getNumber())) missing.add("number");
        if (dto.getRoleId() == null)
            missing.add("roleId");

        if (dto.getMinistryId() == null)
            missing.add("ministryId");

        if (!missing.isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing required field(s): " + String.join(", ", missing));
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
