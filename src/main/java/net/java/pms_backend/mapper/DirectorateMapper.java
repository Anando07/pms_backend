package net.java.pms_backend.mapper;

import net.java.pms_backend.dto.DirectorateDto;
import net.java.pms_backend.entity.Directorate;
import net.java.pms_backend.entity.Ministry;

public class DirectorateMapper {

    public static Directorate mapToDirectorate(DirectorateDto dto, Ministry ministry) {
        return Directorate.builder()
                .id(dto.getId())
                .dirName(dto.getDirName())
                .ministry(ministry)
                .build();
    }

    public static DirectorateDto mapToDirectorateDto(Directorate directorate) {
        return DirectorateDto.builder()
                .id(directorate.getId())
                .dirName(directorate.getDirName())
                .ministryId(directorate.getMinistry() != null ? directorate.getMinistry().getId() : null)
                .ministryName(directorate.getMinistry() != null ? directorate.getMinistry().getMinName() : null)
                .build();
    }
}
