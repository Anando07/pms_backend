package net.java.pms_backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProjectAccessDto {
    private Long id;
    private Long userId;
    private Long projectId;
    private String accessLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

