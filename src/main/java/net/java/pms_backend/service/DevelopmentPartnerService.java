package net.java.pms_backend.service;

import net.java.pms_backend.dto.DevelopmentPartnerDto;
import java.util.List;

public interface DevelopmentPartnerService {
    DevelopmentPartnerDto createDevelopmentPartner(DevelopmentPartnerDto developmentPartnerDto);
    List<DevelopmentPartnerDto> getAllDevelopmentPartners();
    DevelopmentPartnerDto getDevelopmentPartnerById(Long id);
    DevelopmentPartnerDto updateDevelopmentPartner(Long id, DevelopmentPartnerDto developmentPartnerDto);
    void deleteDevelopmentPartner(Long id);
}