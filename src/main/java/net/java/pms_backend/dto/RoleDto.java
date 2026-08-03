package net.java.pms_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {
    private Long id;
    private String roleName;
    private String roleType;
    // Permission names as strings, e.g. "PROJECT_CREATE"
    private java.util.Set<String> permissions;
}