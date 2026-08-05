package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.ReceivedFundDto;
import net.java.pms_backend.entity.Project;
import net.java.pms_backend.entity.ReceivedFund;
import net.java.pms_backend.mapper.ReceivedFundMapper;
import net.java.pms_backend.repository.ProjectRepository;
import net.java.pms_backend.repository.ReceivedFundRepository;
import net.java.pms_backend.service.ReceivedFundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReceivedFundServiceImpl implements ReceivedFundService {

    private final ReceivedFundRepository receivedFundRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public ReceivedFundDto createReceivedFund(ReceivedFundDto dto) {
        validateRequiredFields(dto);

        Project project = resolveProject(dto.getProjectId());

        // Dynamic budget limit: Use Revised Budget if present and > 0; otherwise fallback to Approved Budget
        BigDecimal effectiveBudget = getEffectiveBudget(project);

        BigDecimal currentTotal = receivedFundRepository.getTotalReceivedAmountByProjectId(dto.getProjectId());
        if (currentTotal == null) {
            currentTotal = BigDecimal.ZERO;
        }

        BigDecimal newTotal = currentTotal.add(dto.getFundAmount());

        if (newTotal.compareTo(effectiveBudget) > 0) {
            BigDecimal maxAllowed = effectiveBudget.subtract(currentTotal).max(BigDecimal.ZERO);
            throw new IllegalArgumentException(String.format(
                    "Exceeds project budget limit! Effective Budget (%s): %s Lakhs Tk, Already Received: %s Lakhs Tk, Max Allowed: %s Lakhs Tk, Tried to add: %s Lakhs Tk",
                    isRevisedValid(project) ? "Revised" : "Approved",
                    effectiveBudget, currentTotal, maxAllowed, dto.getFundAmount()));
        }

        ReceivedFund fund = ReceivedFundMapper.mapToReceivedFundEntity(dto, project);
        ReceivedFund savedFund = receivedFundRepository.save(fund);
        return ReceivedFundMapper.mapToReceivedFundDto(savedFund);
    }

    @Override
    public ReceivedFundDto getReceivedFundById(Long id) {
        ReceivedFund fund = findFundOrThrow(id);
        return ReceivedFundMapper.mapToReceivedFundDto(fund);
    }

    @Override
    public List<ReceivedFundDto> getAllReceivedFunds() {
        return receivedFundRepository.findAll().stream()
                .map(ReceivedFundMapper::mapToReceivedFundDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReceivedFundDto> getReceivedFundsByProjectId(Long projectId) {
        resolveProject(projectId);
        return receivedFundRepository.findByProjectId(projectId).stream()
                .map(ReceivedFundMapper::mapToReceivedFundDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReceivedFundDto updateReceivedFund(Long id, ReceivedFundDto dto) {
        validateRequiredFields(dto);

        ReceivedFund existingFund = findFundOrThrow(id);
        Project project = resolveProject(dto.getProjectId());

        BigDecimal effectiveBudget = getEffectiveBudget(project);

        BigDecimal currentTotalOther = receivedFundRepository.getTotalReceivedAmountByProjectIdExcludingId(dto.getProjectId(), id);
        if (currentTotalOther == null) {
            currentTotalOther = BigDecimal.ZERO;
        }

        BigDecimal newTotal = currentTotalOther.add(dto.getFundAmount());

        if (newTotal.compareTo(effectiveBudget) > 0) {
            BigDecimal maxAllowed = effectiveBudget.subtract(currentTotalOther).max(BigDecimal.ZERO);
            throw new IllegalArgumentException(String.format(
                    "Exceeds project budget limit! Effective Budget (%s): %s Lakhs Tk, Received in other records: %s Lakhs Tk, Max Allowed: %s Lakhs Tk, Tried to set: %s Lakhs Tk",
                    isRevisedValid(project) ? "Revised" : "Approved",
                    effectiveBudget, currentTotalOther, maxAllowed, dto.getFundAmount()));
        }

        existingFund.setProject(project);
        existingFund.setFundAmount(dto.getFundAmount());
        existingFund.setFiscalYear(dto.getFiscalYear());
        existingFund.setReceivedDate(dto.getReceivedDate());

        ReceivedFund updatedFund = receivedFundRepository.save(existingFund);
        return ReceivedFundMapper.mapToReceivedFundDto(updatedFund);
    }

    @Override
    @Transactional
    public void deleteReceivedFund(Long id) {
        findFundOrThrow(id);
        receivedFundRepository.deleteById(id);
    }

    // ---- Helper Methods ----

    private ReceivedFund findFundOrThrow(Long id) {
        return receivedFundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Received Fund record not found with ID: " + id));
    }

    private Project resolveProject(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID is required.");
        }
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
    }

    private BigDecimal getEffectiveBudget(Project project) {
        if (isRevisedValid(project)) {
            return project.getRevisedBudget();
        }
        return project.getApprovedBudget() != null ? project.getApprovedBudget() : BigDecimal.ZERO;
    }

    private boolean isRevisedValid(Project project) {
        return project.getRevisedBudget() != null && project.getRevisedBudget().compareTo(BigDecimal.ZERO) > 0;
    }

    private void validateRequiredFields(ReceivedFundDto dto) {
        List<String> missing = new ArrayList<>();

        if (dto.getProjectId() == null) missing.add("projectId");
        if (dto.getFundAmount() == null || dto.getFundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            missing.add("valid fundAmount (>0)");
        }
        if (isBlank(dto.getFiscalYear())) missing.add("fiscalYear");
        if (dto.getReceivedDate() == null) missing.add("receivedDate");

        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Missing or invalid required field(s): " + String.join(", ", missing));
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}