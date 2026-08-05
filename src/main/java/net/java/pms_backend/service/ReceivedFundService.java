package net.java.pms_backend.service;

import net.java.pms_backend.dto.ReceivedFundDto;
import java.util.List;

public interface ReceivedFundService {
    ReceivedFundDto createReceivedFund(ReceivedFundDto receivedFundDto);
    ReceivedFundDto getReceivedFundById(Long id);
    List<ReceivedFundDto> getAllReceivedFunds();
    List<ReceivedFundDto> getReceivedFundsByProjectId(Long projectId);
    ReceivedFundDto updateReceivedFund(Long id, ReceivedFundDto receivedFundDto);
    void deleteReceivedFund(Long id);
}