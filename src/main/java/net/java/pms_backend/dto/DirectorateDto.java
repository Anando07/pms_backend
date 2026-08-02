package net.java.pms_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectorateDto {
    private Long id;
    private String dirName;
    private Long ministryId;
    private String ministryName;
}
