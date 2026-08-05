package net.java.pms_backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.FinancialProgressDto;
import net.java.pms_backend.service.FinancialProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/financial-progress")
@AllArgsConstructor
public class FinancialProgressController {

    private final FinancialProgressService financialProgressService;

    @PostMapping
    public ResponseEntity<FinancialProgressDto> createExpense(@Valid @RequestBody FinancialProgressDto dto) {
        return new ResponseEntity<>(financialProgressService.createExpense(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialProgressDto> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(financialProgressService.getExpenseById(id));
    }

    @GetMapping
    public ResponseEntity<List<FinancialProgressDto>> getAllExpenses() {
        return ResponseEntity.ok(financialProgressService.getAllExpenses());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<FinancialProgressDto>> getExpensesByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(financialProgressService.getExpensesByProjectId(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialProgressDto> updateExpense(@PathVariable Long id, @Valid @RequestBody FinancialProgressDto dto) {
        return ResponseEntity.ok(financialProgressService.updateExpense(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        financialProgressService.deleteExpense(id);
        return ResponseEntity.ok("Expense record deleted successfully.");
    }
}