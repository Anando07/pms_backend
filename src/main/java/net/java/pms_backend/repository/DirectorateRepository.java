package net.java.pms_backend.repository;

import net.java.pms_backend.entity.Directorate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectorateRepository extends JpaRepository<Directorate, Long> {

    List<Directorate> findByMinistryId(Long ministryId);

    boolean existsByDirNameIgnoreCaseAndMinistryId(String dirName, Long ministryId);
}
