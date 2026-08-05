package net.java.pms_backend.service;

import net.java.pms_backend.dto.DirectorDto;

import java.util.List;

public interface DirectorService {
    DirectorDto createDirector(DirectorDto directorDto);
    List<DirectorDto> getAllDirectors();
    DirectorDto getDirectorById(Long id);
    List<DirectorDto> getDirectorsByProjectId(Long projectId);
    DirectorDto updateDirector(Long id, DirectorDto directorDto);
    void deleteDirector(Long id);
}