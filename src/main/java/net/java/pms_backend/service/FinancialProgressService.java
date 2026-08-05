package net.java.pms_backend.service;

import net.java.pms_backend.dto.FinancialProgressDto;

import java.util.List;

public interface FinancialProgressService {
    FinancialProgressDto createExpense(FinancialProgressDto dto);
    FinancialProgressDto getExpenseById(Long id);
    List<FinancialProgressDto> getAllExpenses();
    List<FinancialProgressDto> getExpensesByProjectId(Long projectId);
    FinancialProgressDto updateExpense(Long id, FinancialProgressDto dto);
    void deleteExpense(Long id);
}