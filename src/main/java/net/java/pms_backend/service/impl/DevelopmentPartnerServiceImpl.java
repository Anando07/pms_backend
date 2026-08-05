package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.DevelopmentPartnerDto;
import net.java.pms_backend.entity.DevelopmentPartner;
import net.java.pms_backend.exception.ResourceNotFoundException;
import net.java.pms_backend.repository.DevelopmentPartnerRepository;
import net.java.pms_backend.service.DevelopmentPartnerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DevelopmentPartnerServiceImpl implements DevelopmentPartnerService {

    private final DevelopmentPartnerRepository repository;

    @Override
    public List<DevelopmentPartnerDto> getAllDevelopmentPartners() {
        return repository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DevelopmentPartnerDto createDevelopmentPartner(DevelopmentPartnerDto dto) {
        DevelopmentPartner entity = mapToEntity(dto);
        DevelopmentPartner savedEntity = repository.save(entity);
        return mapToDto(savedEntity);
    }

    @Override
    public DevelopmentPartnerDto getDevelopmentPartnerById(Long id) {
        DevelopmentPartner entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Development Partner not found with id: " + id));
        return mapToDto(entity);
    }

    @Override
    public DevelopmentPartnerDto updateDevelopmentPartner(Long id, DevelopmentPartnerDto dto) {
        DevelopmentPartner entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Development Partner not found with id: " + id));

        entity.setDevPartnerName(dto.getDevPartnerName());
        DevelopmentPartner updatedEntity = repository.save(entity);
        return mapToDto(updatedEntity);
    }

    @Override
    public void deleteDevelopmentPartner(Long id) {
        DevelopmentPartner entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Development Partner not found with id: " + id));
        repository.delete(entity);
    }

    private DevelopmentPartnerDto mapToDto(DevelopmentPartner entity) {
        if (entity == null) return null;
        return DevelopmentPartnerDto.builder()
                .id(entity.getId())
                .devPartnerName(entity.getDevPartnerName())
                .build();
    }

    private DevelopmentPartner mapToEntity(DevelopmentPartnerDto dto) {
        if (dto == null) return null;
        return DevelopmentPartner.builder()
                .id(dto.getId())
                .devPartnerName(dto.getDevPartnerName())
                .build();
    }
}