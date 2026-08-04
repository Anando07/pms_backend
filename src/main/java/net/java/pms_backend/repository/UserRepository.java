package net.java.pms_backend.repository;

import net.java.pms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    // Finds user by email or mobile number
    Optional<User> findByEmailOrNumber(String email, String number);
}
