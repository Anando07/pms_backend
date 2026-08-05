package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.FinancialProgressDto;
import net.java.pms_backend.entity.FinancialProgress;
import net.java.pms_backend.entity.Project;
import net.java.pms_backend.exception.ResourceNotFoundException;
import net.java.pms_backend.mapper.FinancialProgressMapper;
import net.java.pms_backend.repository.FinancialProgressRepository;
import net.java.pms_backend.repository.ProjectRepository;
import net.java.pms_backend.service.FinancialProgressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinancialProgressServiceImpl implements FinancialProgressService {

    private final FinancialProgressRepository financialProgressRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public FinancialProgressDto createExpense(FinancialProgressDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dto.getProjectId()));

        BigDecimal effectiveBudget = project.getEffectiveBudget();
        BigDecimal currentTotal = financialProgressRepository.getTotalExpenseByProjectId(dto.getProjectId());
        if (currentTotal == null) {
            currentTotal = BigDecimal.ZERO;
        }

        BigDecimal newTotal = currentTotal.add(dto.getExpenseAmount());

        if (newTotal.compareTo(effectiveBudget) > 0) {
            BigDecimal maxAllowed = effectiveBudget.subtract(currentTotal).max(BigDecimal.ZERO);
            throw new IllegalArgumentException(String.format(
                    "Exceeds project budget limit! Effective Budget: %s Lakhs Tk, Already Spent: %s Lakhs Tk, Max Allowed: %s Lakhs Tk, Tried to add: %s Lakhs Tk",
                    effectiveBudget, currentTotal, maxAllowed, dto.getExpenseAmount()));
        }

        FinancialProgress entity = FinancialProgressMapper.mapToEntity(dto, project);
        FinancialProgress saved = financialProgressRepository.save(entity);
        return FinancialProgressMapper.mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialProgressDto getExpenseById(Long id) {
        FinancialProgress entity = financialProgressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense record not found with ID: " + id));
        return FinancialProgressMapper.mapToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialProgressDto> getAllExpenses() {
        return financialProgressRepository.findAll().stream()
                .map(FinancialProgressMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialProgressDto> getExpensesByProjectId(Long projectId) {
        return financialProgressRepository.findByProjectId(projectId).stream()
                .map(FinancialProgressMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FinancialProgressDto updateExpense(Long id, FinancialProgressDto dto) {
        FinancialProgress existing = financialProgressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense record not found with ID: " + id));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dto.getProjectId()));

        BigDecimal effectiveBudget = project.getEffectiveBudget();
        BigDecimal currentTotalOther = financialProgressRepository.getTotalExpenseByProjectIdExcludingId(dto.getProjectId(), id);
        if (currentTotalOther == null) {
            currentTotalOther = BigDecimal.ZERO;
        }

        BigDecimal newTotal = currentTotalOther.add(dto.getExpenseAmount());

        if (newTotal.compareTo(effectiveBudget) > 0) {
            BigDecimal maxAllowed = effectiveBudget.subtract(currentTotalOther).max(BigDecimal.ZERO);
            throw new IllegalArgumentException(String.format(
                    "Exceeds project budget limit! Effective Budget: %s Lakhs Tk, Spent in other records: %s Lakhs Tk, Max Allowed: %s Lakhs Tk, Tried to set: %s Lakhs Tk",
                    effectiveBudget, currentTotalOther, maxAllowed, dto.getExpenseAmount()));
        }

        existing.setProject(project);
        existing.setExpenseAmount(dto.getExpenseAmount());
        existing.setPurpose(dto.getPurpose());
        existing.setExpenseDate(dto.getExpenseDate());

        FinancialProgress updated = financialProgressRepository.save(existing);
        return FinancialProgressMapper.mapToDto(updated);
    }

    @Override
    @Transactional
    public void deleteExpense(Long id) {
        if (!financialProgressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense record not found with ID: " + id);
        }
        financialProgressRepository.deleteById(id);
    }
}