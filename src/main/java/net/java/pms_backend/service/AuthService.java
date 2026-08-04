package net.java.pms_backend.service;

import net.java.pms_backend.dto.JwtResponse;
import net.java.pms_backend.dto.LoginRequest;
import net.java.pms_backend.entity.Passcode;
import net.java.pms_backend.entity.User;
import net.java.pms_backend.repository.PasscodeRepository;
import net.java.pms_backend.repository.UserRepository;
import net.java.pms_backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasscodeRepository passcodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest loginRequest) {
        // 1. Fetch User by Email or Mobile Number
        User user = userRepository.findByEmailOrNumber(loginRequest.getUsername(), loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid email/number"));

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new BadCredentialsException("User account is deactivated");
        }

        // 2. Fetch User's Active Passcode
        Passcode passcodeEntity = passcodeRepository.findByUserAndActiveTrue(user)
                .orElseThrow(() -> new BadCredentialsException("No active Password found for this user"));

        // 3. Verify Passcode Expiration
        if (passcodeEntity.getExpiresAt() != null && passcodeEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Password has expired");
        }

        // 4. Validate Encrypted Passcode
        if (!passwordEncoder.matches(loginRequest.getPasscode(), passcodeEntity.getPasscode())) {
            throw new BadCredentialsException("Invalid Password");
        }

        // 5. Build Permissions Set
        Set<String> permissions = user.getRole() != null && user.getRole().getPermissions() != null
                ? user.getRole().getPermissions().stream().map(Enum::name).collect(Collectors.toSet())
                : Set.of();

        String roleName = user.getRole() != null ? user.getRole().getRoleName() : "USER";

        // 6. Generate JWT Token with Claims
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", roleName);
        extraClaims.put("permissions", permissions);

        String jwtToken = jwtService.generateToken(user.getEmail(), extraClaims);

        return JwtResponse.builder()
                .token(jwtToken)
                .type("Bearer")
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .number(user.getNumber())
                .role(roleName)
                .permissions(permissions)
                .build();
    }
}