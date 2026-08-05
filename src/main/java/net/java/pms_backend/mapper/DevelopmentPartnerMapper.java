package net.java.pms_backend.mapper;

import net.java.pms_backend.entity.DevelopmentPartner;
import net.java.pms_backend.dto.DevelopmentPartnerDto;

public class DevelopmentPartnerMapper {

    private DevelopmentPartnerMapper() {}

    public static DevelopmentPartnerDto mapToDevelopmentPartnerDto(DevelopmentPartner developmentPartner) {
        if (developmentPartner == null) return null;
        return DevelopmentPartnerDto.builder()
                .id(developmentPartner.getId())
                .devPartnerName(developmentPartner.getDevPartnerName())
                .build();
    }

    public static DevelopmentPartner mapToDevelopmentPartner(DevelopmentPartnerDto developmentPartnerDto) {
        if (developmentPartnerDto == null) return null;
        return DevelopmentPartner.builder()
                .id(developmentPartnerDto.getId())
                .devPartnerName(developmentPartnerDto.getDevPartnerName())
                .build();
    }
}
