package net.java.pms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "development_partners")
public class DevelopmentPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dev_partner_name", nullable = false, unique = true, length = 255)
    private String devPartnerName;
}