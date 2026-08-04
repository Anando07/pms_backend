package net.java.pms_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Identifier (Email or Phone Number) is required")
    private String username; // Accepts user email or number

    @NotBlank(message = "Passcode is required")
    private String passcode;
}