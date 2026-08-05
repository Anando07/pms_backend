package net.java.pms_backend.repository;

import net.java.pms_backend.entity.DevelopmentPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DevelopmentPartnerRepository extends JpaRepository<DevelopmentPartner, Long> {
    boolean existsByDevPartnerName(String devPartnerName);
    Optional<DevelopmentPartner> findByDevPartnerName(String devPartnerName);
}