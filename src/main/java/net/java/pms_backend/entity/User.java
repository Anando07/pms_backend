package net.java.pms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String officeName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String number;

    @ManyToOne
    @JoinColumn(name="ministry_id")
    private Ministry ministry;

    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationship with Role (Many Users -> One Role)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    // Relationship with Passcode (One User -> One Passcode)
    // Kept on the entity, but intentionally not exposed through UserDto/UserMapper --
    // passcode is not needed on the Users screen.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Passcode passcode;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "profile_images", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "image_data", columnDefinition = "LONGTEXT")
    @Builder.Default
    private List<String> profileImages = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
