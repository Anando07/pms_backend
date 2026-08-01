package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.MinistryDto;
import net.java.pms_backend.entity.Ministry;

public class MinistryMapper {

    private MinistryMapper() {}

    public static MinistryDto mapToMinistryDto(Ministry ministry) {
        if (ministry == null) return null;
        return MinistryDto.builder()
                .id(ministry.getId())
                .name(ministry.getName())
                .build();
    }

    public static Ministry mapToMinistry(MinistryDto ministryDto) {
        if (ministryDto == null) return null;
        return Ministry.builder()
                .id(ministryDto.getId())
                .name(ministryDto.getName())
                .build();
    }
}