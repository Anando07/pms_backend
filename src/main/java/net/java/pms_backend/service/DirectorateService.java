package net.java.pms_backend.service;

import net.java.pms_backend.dto.DirectorateDto;

import java.util.List;

public interface DirectorateService {
    DirectorateDto createDirectorate(DirectorateDto directorateDto);
    List<DirectorateDto> getAllDirectorates();
    List<DirectorateDto> getDirectoratesByMinistry(Long ministryId);
    DirectorateDto getDirectorateById(Long id);
    DirectorateDto updateDirectorate(Long id, DirectorateDto directorateDto);
    void deleteDirectorate(Long id);
}
