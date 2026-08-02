package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.DirectorateDto;
import net.java.pms_backend.entity.Directorate;
import net.java.pms_backend.entity.Ministry;
import net.java.pms_backend.exception.DuplicateResourceException;
import net.java.pms_backend.exception.ResourceNotFoundException;
import net.java.pms_backend.mapper.DirectorateMapper;
import net.java.pms_backend.repository.DirectorateRepository;
import net.java.pms_backend.repository.MinistryRepository;
import net.java.pms_backend.service.DirectorateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DirectorateServiceImpl implements DirectorateService {

    private final DirectorateRepository directorateRepository;
    private final MinistryRepository ministryRepository;

    @Override
    public DirectorateDto createDirectorate(DirectorateDto directorateDto) {
        Ministry ministry = ministryRepository.findById(directorateDto.getMinistryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ministry not found with id: " + directorateDto.getMinistryId()));

        if (directorateRepository.existsByDirNameIgnoreCaseAndMinistryId(
                directorateDto.getDirName(), directorateDto.getMinistryId())) {
            throw new DuplicateResourceException(
                    "A directorate named '" + directorateDto.getDirName() +
                            "' already exists under this ministry.");
        }

        Directorate directorate = DirectorateMapper.mapToDirectorate(directorateDto, ministry);
        Directorate saved = directorateRepository.save(directorate);
        return DirectorateMapper.mapToDirectorateDto(saved);
    }

    @Override
    public List<DirectorateDto> getAllDirectorates() {
        return directorateRepository.findAll().stream()
                .map(DirectorateMapper::mapToDirectorateDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DirectorateDto> getDirectoratesByMinistry(Long ministryId) {
        return directorateRepository.findByMinistryId(ministryId).stream()
                .map(DirectorateMapper::mapToDirectorateDto)
                .collect(Collectors.toList());
    }

    @Override
    public DirectorateDto getDirectorateById(Long id) {
        Directorate directorate = directorateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Directorate not found with id: " + id));
        return DirectorateMapper.mapToDirectorateDto(directorate);
    }

    @Override
    public DirectorateDto updateDirectorate(Long id, DirectorateDto directorateDto) {
        Directorate existing = directorateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Directorate not found with id: " + id));

        Ministry ministry = ministryRepository.findById(directorateDto.getMinistryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ministry not found with id: " + directorateDto.getMinistryId()));

        boolean nameChanged = !existing.getDirName().equalsIgnoreCase(directorateDto.getDirName());
        boolean ministryChanged = !existing.getMinistry().getId().equals(directorateDto.getMinistryId());

        if ((nameChanged || ministryChanged) &&
                directorateRepository.existsByDirNameIgnoreCaseAndMinistryId(
                        directorateDto.getDirName(), directorateDto.getMinistryId())) {
            throw new DuplicateResourceException(
                    "A directorate named '" + directorateDto.getDirName() +
                            "' already exists under this ministry.");
        }

        existing.setDirName(directorateDto.getDirName());
        existing.setMinistry(ministry);

        Directorate updated = directorateRepository.save(existing);
        return DirectorateMapper.mapToDirectorateDto(updated);
    }

    @Override
    public void deleteDirectorate(Long id) {
        Directorate existing = directorateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Directorate not found with id: " + id));
        directorateRepository.delete(existing);
    }
}
