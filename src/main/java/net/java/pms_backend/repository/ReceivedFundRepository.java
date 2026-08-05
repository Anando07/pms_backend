package net.java.pms_backend.repository;

import net.java.pms_backend.entity.ReceivedFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReceivedFundRepository extends JpaRepository<ReceivedFund, Long> {

    List<ReceivedFund> findByProjectId(Long projectId);

    @Query("SELECT COALESCE(SUM(r.fundAmount), 0) FROM ReceivedFund r WHERE r.project.id = :projectId")
    BigDecimal getTotalReceivedAmountByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COALESCE(SUM(r.fundAmount), 0) FROM ReceivedFund r WHERE r.project.id = :projectId AND r.id != :excludedId")
    BigDecimal getTotalReceivedAmountByProjectIdExcludingId(@Param("projectId") Long projectId, @Param("excludedId") Long excludedId);
}