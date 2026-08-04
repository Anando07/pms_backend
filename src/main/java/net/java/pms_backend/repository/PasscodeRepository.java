package net.java.pms_backend.repository;

import net.java.pms_backend.entity.Passcode;
import net.java.pms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasscodeRepository extends JpaRepository<Passcode, Long> {
    Optional<Passcode> findByUserId(Long userId);
    Optional<Passcode> findByUserAndActiveTrue(User user);
}