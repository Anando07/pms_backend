package net.java.pms_backend.repository;

import net.java.pms_backend.entity.FinancialProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FinancialProgressRepository extends JpaRepository<FinancialProgress, Long> {

    List<FinancialProgress> findByProjectId(Long projectId);

    @Query("SELECT SUM(f.expenseAmount) FROM FinancialProgress f WHERE f.project.id = :projectId")
    BigDecimal getTotalExpenseByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT SUM(f.expenseAmount) FROM FinancialProgress f WHERE f.project.id = :projectId AND f.id != :excludedId")
    BigDecimal getTotalExpenseByProjectIdExcludingId(@Param("projectId") Long projectId, @Param("excludedId") Long excludedId);
}