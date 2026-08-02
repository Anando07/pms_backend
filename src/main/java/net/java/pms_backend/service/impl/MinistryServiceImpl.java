package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.MinistryDto;
import net.java.pms_backend.entity.Ministry;
import net.java.pms_backend.exception.ResourceNotFoundException;
import net.java.pms_backend.mapper.MinistryMapper;
import net.java.pms_backend.repository.MinistryRepository;
import net.java.pms_backend.service.MinistryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MinistryServiceImpl implements MinistryService {

    private final MinistryRepository ministryRepository;

    @Override
    public MinistryDto createMinistry(MinistryDto ministryDto) {
        Ministry ministry = MinistryMapper.mapToMinistry(ministryDto);
        Ministry savedMinistry = ministryRepository.save(ministry);
        return MinistryMapper.mapToMinistryDto(savedMinistry);
    }

    @Override
    public List<MinistryDto> getAllMinistries() {
        List<Ministry> ministries = ministryRepository.findAll();
        return ministries.stream()
                .map(MinistryMapper::mapToMinistryDto)
                .collect(Collectors.toList());
    }

    @Override
    public MinistryDto getMinistryById(Long id) {
        Ministry ministry = ministryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found with id: " + id));
        return MinistryMapper.mapToMinistryDto(ministry);
    }

    @Override
    public MinistryDto updateMinistry(Long id, MinistryDto ministryDto) {
        Ministry existingMinistry = ministryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found with id: " + id));

        existingMinistry.setMinName(ministryDto.getMinName());

        Ministry updatedMinistry = ministryRepository.save(existingMinistry);
        return MinistryMapper.mapToMinistryDto(updatedMinistry);
    }

    @Override
    public void deleteMinistry(Long id) {
        Ministry existingMinistry = ministryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found with id: " + id));
        ministryRepository.delete(existingMinistry);
    }
}