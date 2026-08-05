package net.java.pms_backend.repository;

import net.java.pms_backend.entity.Finance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {

    @Override
    @EntityGraph(attributePaths = {"project", "developmentPartner"})
    List<Finance> findAll();

    @Override
    @EntityGraph(attributePaths = {"project", "developmentPartner"})
    Optional<Finance> findById(Long id);

    @EntityGraph(attributePaths = {"project", "developmentPartner"})
    List<Finance> findByProjectId(Long projectId);

    @Query("SELECT COALESCE(SUM(f.totalApprovedFund), 0) FROM Finance f WHERE f.project.id = :projectId AND (:excludeId IS NULL OR f.id <> :excludeId)")
    BigDecimal sumApprovedFundByProjectIdExcludingId(@Param("projectId") Long projectId, @Param("excludeId") Long excludeId);

    @Query("SELECT COALESCE(SUM(f.totalRevisedFund), 0) FROM Finance f WHERE f.project.id = :projectId AND (:excludeId IS NULL OR f.id <> :excludeId)")
    BigDecimal sumRevisedFundByProjectIdExcludingId(@Param("projectId") Long projectId, @Param("excludeId") Long excludeId);
}