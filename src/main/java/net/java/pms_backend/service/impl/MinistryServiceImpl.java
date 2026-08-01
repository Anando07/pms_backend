package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.MinistryDto;
import net.java.pms_backend.entity.Ministry;
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
}