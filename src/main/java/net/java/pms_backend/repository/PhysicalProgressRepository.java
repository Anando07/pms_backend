package net.java.pms_backend.repository;

import net.java.pms_backend.entity.PhysicalProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PhysicalProgressRepository extends JpaRepository<PhysicalProgress, Long> {

    List<PhysicalProgress> findByProjectId(Long projectId);

    @Query("SELECT COALESCE(SUM(p.completedPercentage), 0) FROM PhysicalProgress p " +
            "WHERE p.projectWorkParameter.id = :parameterId")
    BigDecimal getTotalLoggedByParameterId(@Param("parameterId") Long parameterId);

    @Query("SELECT COALESCE(SUM(p.completedPercentage), 0) FROM PhysicalProgress p " +
            "WHERE p.projectWorkParameter.id = :parameterId AND p.id <> :excludeId")
    BigDecimal getTotalLoggedByParameterIdExcludingId(
            @Param("parameterId") Long parameterId, @Param("excludeId") Long excludeId);
}