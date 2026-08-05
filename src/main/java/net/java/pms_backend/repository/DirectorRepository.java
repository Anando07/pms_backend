package net.java.pms_backend.repository;

import net.java.pms_backend.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

    List<Director> findByProjectId(Long projectId);

    boolean existsByDirNameIgnoreCaseAndProjectIdAndIdNot(String dirName, Long projectId, Long id);

    boolean existsByDirNameIgnoreCaseAndProjectId(String dirName, Long projectId);
}