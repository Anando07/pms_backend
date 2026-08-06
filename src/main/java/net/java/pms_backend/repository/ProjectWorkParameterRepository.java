package net.java.pms_backend.repository;

import net.java.pms_backend.entity.ProjectWorkParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectWorkParameterRepository extends JpaRepository<ProjectWorkParameter, Long> {
    List<ProjectWorkParameter> findByProjectId(Long projectId);
}