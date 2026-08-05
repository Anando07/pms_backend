package net.java.pms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevelopmentPartnerDto {

    private Long id;

    @NotBlank(message = "Development partner name is required")
    @Size(max = 255, message = "Development partner name must not exceed 255 characters")
    private String devPartnerName;
}