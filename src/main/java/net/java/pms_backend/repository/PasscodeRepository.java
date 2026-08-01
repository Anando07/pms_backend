package net.java.pms_backend.repository;

import net.java.pms_backend.entity.Passcode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasscodeRepository extends JpaRepository<Passcode, Long> {
}
