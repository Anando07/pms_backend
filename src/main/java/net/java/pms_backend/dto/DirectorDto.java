package net.java.pms_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectorDto {

    private Long id;

    @NotBlank(message = "Director name is required")
    private String dirName;

    @NotBlank(message = "Designation is required")
    private String dirDesignation;

    @NotNull(message = "Ministry ID is required")
    private Long ministryId;

    private Long directorateId;

    @NotBlank(message = "Contact is required")
    private String contact;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotBlank(message = "Duty Role is required")
    private String dutyRole;

    @NotNull(message = "Assigned Date is required")
    private LocalDate assignedDate;

    private LocalDate releaseDate;
    private String image;

    // Response Display Fields
    private String ministryName;
    private String directorateName;
    private String projectName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}