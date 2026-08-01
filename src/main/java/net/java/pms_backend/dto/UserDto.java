package net.java.pms_backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String name;
    private String designation;
    private String officeName;
    private String email;
    private String number;
    private String minDiv;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Role Details
    private Long roleId;
    private String roleName;

    // Passcode Details (Optional/Flattened)
    private String passcode;
    private Boolean passcodeActive;
    private LocalDateTime passcodeExpiresAt;
}