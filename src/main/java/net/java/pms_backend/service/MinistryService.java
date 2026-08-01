package net.java.pms_backend.service;

import net.java.pms_backend.dto.MinistryDto;
import java.util.List;

public interface MinistryService {
    MinistryDto createMinistry(MinistryDto ministryDto);
    List<MinistryDto> getAllMinistries();
}