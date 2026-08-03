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

    // Required fields
    private String name;
    private String designation;
    private String officeName;
    private String email;
    private String number;
    private String minDiv;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Profile image (single, optional). Named "avatar" to match the JSON key
    // Users.jsx sends (formData.avatar) and reads (user.avatar).
    private String avatar;

    // Role Details
    private Long roleId;
    private String roleName;

    // Passcode intentionally removed -- not needed on the Users screen.
}
