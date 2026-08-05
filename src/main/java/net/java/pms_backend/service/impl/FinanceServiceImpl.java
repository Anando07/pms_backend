package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.FinanceDto;
import net.java.pms_backend.entity.DevelopmentPartner;
import net.java.pms_backend.entity.Finance;
import net.java.pms_backend.entity.Project;
import net.java.pms_backend.exception.BadRequestException;
import net.java.pms_backend.exception.ResourceNotFoundException;
import net.java.pms_backend.mapper.FinanceMapper;
import net.java.pms_backend.repository.DevelopmentPartnerRepository;
import net.java.pms_backend.repository.FinanceRepository;
import net.java.pms_backend.repository.ProjectRepository;
import net.java.pms_backend.service.FinanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final FinanceRepository financeRepository;
    private final ProjectRepository projectRepository;
    private final DevelopmentPartnerRepository devPartnerRepository;
    private final FinanceMapper financeMapper;

    @Override
    @Transactional
    public FinanceDto createFinance(FinanceDto financeDto) {
        Project project = projectRepository.findById(financeDto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + financeDto.getProjectId()));

        DevelopmentPartner devPartner = devPartnerRepository.findById(financeDto.getDevelopmentPartnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Development Partner not found with id: " + financeDto.getDevelopmentPartnerId()));

        validateFinancialLimits(project, financeDto, null);

        Finance finance = financeMapper.toEntity(financeDto, project, devPartner);
        Finance savedFinance = financeRepository.save(finance);

        return financeMapper.toDto(savedFinance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinanceDto> getAllFinances() {
        return financeRepository.findAll().stream()
                .map(financeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FinanceDto getFinanceById(Long id) {
        Finance finance = financeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finance record not found with id: " + id));
        return financeMapper.toDto(finance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinanceDto> getFinancesByProjectId(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        return financeRepository.findByProjectId(projectId).stream()
                .map(financeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FinanceDto updateFinance(Long id, FinanceDto financeDto) {
        Finance existingFinance = financeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finance record not found with id: " + id));

        Project project = projectRepository.findById(financeDto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + financeDto.getProjectId()));

        DevelopmentPartner devPartner = devPartnerRepository.findById(financeDto.getDevelopmentPartnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Development Partner not found with id: " + financeDto.getDevelopmentPartnerId()));

        validateFinancialLimits(project, financeDto, id);

        existingFinance.setProject(project);
        existingFinance.setDevelopmentPartner(devPartner);
        existingFinance.setTotalApprovedFund(financeDto.getTotalApprovedFund() != null ? financeDto.getTotalApprovedFund() : BigDecimal.ZERO);
        existingFinance.setTotalRevisedFund(financeDto.getTotalRevisedFund() != null ? financeDto.getTotalRevisedFund() : BigDecimal.ZERO);

        Finance updatedFinance = financeRepository.save(existingFinance);
        return financeMapper.toDto(updatedFinance);
    }

    @Override
    @Transactional
    public void deleteFinance(Long id) {
        Finance finance = financeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finance record not found with id: " + id));
        financeRepository.delete(finance);
    }

    private void validateFinancialLimits(Project project, FinanceDto dto, Long currentRecordId) {
        BigDecimal appFund = dto.getTotalApprovedFund() != null ? dto.getTotalApprovedFund() : BigDecimal.ZERO;
        BigDecimal revFund = dto.getTotalRevisedFund() != null ? dto.getTotalRevisedFund() : BigDecimal.ZERO;

        if (appFund.compareTo(BigDecimal.ZERO) <= 0 && revFund.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Please enter either an Approved Fund or a Revised Fund amount.");
        }

        boolean hasProjectRevisedBudget = project.getRevisedBudget() != null && project.getRevisedBudget().compareTo(BigDecimal.ZERO) > 0;
        if (!hasProjectRevisedBudget) {
            dto.setTotalRevisedFund(BigDecimal.ZERO);
            revFund = BigDecimal.ZERO;
        }

        BigDecimal existingApprovedSum = financeRepository.sumApprovedFundByProjectIdExcludingId(project.getId(), currentRecordId);
        BigDecimal availableApprovedPool = project.getApprovedBudget().subtract(existingApprovedSum);

        if (appFund.compareTo(availableApprovedPool) > 0) {
            throw new BadRequestException("Approved fund (" + appFund + ") exceeds available unallocated project budget (" + availableApprovedPool + ").");
        }

        if (hasProjectRevisedBudget && revFund.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal existingRevisedSum = financeRepository.sumRevisedFundByProjectIdExcludingId(project.getId(), currentRecordId);
            BigDecimal availableRevisedPool = project.getRevisedBudget().subtract(existingRevisedSum);

            if (revFund.compareTo(availableRevisedPool) > 0) {
                throw new BadRequestException("Revised fund (" + revFund + ") exceeds available unallocated revised budget (" + availableRevisedPool + ").");
            }
        }
    }
}