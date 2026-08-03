package net.java.pms_backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMinistryAccessDto {
    private Long id;
    private Long userId;
    private Long ministryId;
    private String accessLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

