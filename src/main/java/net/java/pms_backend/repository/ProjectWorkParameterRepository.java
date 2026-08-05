package net.java.pms_backend.repository;

import net.java.pms_backend.entity.ProjectWorkParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectWorkParameterRepository extends JpaRepository<ProjectWorkParameter, Long> {

    List<ProjectWorkParameter> findByProjectId(Long projectId);

    @Modifying
    @Query("DELETE FROM ProjectWorkParameter p WHERE p.project.id = :projectId")
    void deleteByProjectIdDirectly(@Param("projectId") Long projectId);
}