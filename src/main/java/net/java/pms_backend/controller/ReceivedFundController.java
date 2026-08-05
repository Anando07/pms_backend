package net.java.pms_backend.controller;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.ReceivedFundDto;
import net.java.pms_backend.service.ReceivedFundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/received-funds")
public class ReceivedFundController {

    private final ReceivedFundService receivedFundService;

    @PostMapping
    public ResponseEntity<?> createReceivedFund(@RequestBody ReceivedFundDto dto) {
        try {
            ReceivedFundDto saved = receivedFundService.createReceivedFund(dto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ReceivedFundDto>> getAllReceivedFunds() {
        return ResponseEntity.ok(receivedFundService.getAllReceivedFunds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceivedFundDto> getReceivedFundById(@PathVariable Long id) {
        return ResponseEntity.ok(receivedFundService.getReceivedFundById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ReceivedFundDto>> getReceivedFundsByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(receivedFundService.getReceivedFundsByProjectId(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReceivedFund(@PathVariable Long id, @RequestBody ReceivedFundDto dto) {
        try {
            ReceivedFundDto updated = receivedFundService.updateReceivedFund(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceivedFund(@PathVariable Long id) {
        receivedFundService.deleteReceivedFund(id);
        return ResponseEntity.noContent().build();
    }
}