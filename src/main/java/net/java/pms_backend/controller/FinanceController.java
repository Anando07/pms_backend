package net.java.pms_backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.FinanceDto;
import net.java.pms_backend.service.FinanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/finances")
public class FinanceController {

    private final FinanceService financeService;

    @PostMapping
    public ResponseEntity<FinanceDto> createFinance(@Valid @RequestBody FinanceDto financeDto) {
        FinanceDto savedFinance = financeService.createFinance(financeDto);
        return new ResponseEntity<>(savedFinance, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FinanceDto>> getAllFinances() {
        return ResponseEntity.ok(financeService.getAllFinances());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinanceDto> getFinanceById(@PathVariable Long id) {
        return ResponseEntity.ok(financeService.getFinanceById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<FinanceDto>> getFinancesByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(financeService.getFinancesByProjectId(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinanceDto> updateFinance(
            @PathVariable Long id,
            @Valid @RequestBody FinanceDto financeDto) {
        return ResponseEntity.ok(financeService.updateFinance(id, financeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinance(@PathVariable Long id) {
        financeService.deleteFinance(id);
        return ResponseEntity.noContent().build();
    }
}