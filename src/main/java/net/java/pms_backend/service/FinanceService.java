package net.java.pms_backend.service;

import net.java.pms_backend.dto.FinanceDto;

import java.util.List;

public interface FinanceService {
    FinanceDto createFinance(FinanceDto financeDto);
    List<FinanceDto> getAllFinances();
    FinanceDto getFinanceById(Long id);
    List<FinanceDto> getFinancesByProjectId(Long projectId);
    FinanceDto updateFinance(Long id, FinanceDto financeDto);
    void deleteFinance(Long id);
}