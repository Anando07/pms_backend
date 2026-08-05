package net.java.pms_backend.service.impl;

import lombok.AllArgsConstructor;
import net.java.pms_backend.dto.DirectorDto;
import net.java.pms_backend.entity.Director;
import net.java.pms_backend.entity.Directorate;
import net.java.pms_backend.entity.Ministry;
import net.java.pms_backend.entity.Project;
import net.java.pms_backend.mapper.DirectorMapper;
import net.java.pms_backend.repository.DirectorRepository;
import net.java.pms_backend.repository.DirectorateRepository;
import net.java.pms_backend.repository.MinistryRepository;
import net.java.pms_backend.repository.ProjectRepository;
import net.java.pms_backend.service.DirectorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;
    private final MinistryRepository ministryRepository;
    private final DirectorateRepository directorateRepository;
    private final ProjectRepository projectRepository;

    @Override
    public DirectorDto createDirector(DirectorDto directorDto) {
        validateRequiredFields(directorDto);

        Ministry ministry = resolveMinistry(directorDto.getMinistryId(), true);
        Directorate directorate = resolveDirectorate(directorDto.getDirectorateId(), false);
        Project project = resolveProject(directorDto.getProjectId(), true);

        // DirectorMapper.mapToDirectorEntity maps all incoming fields including image
        Director director = DirectorMapper.mapToDirectorEntity(directorDto, ministry, directorate, project);

        Director savedDirector = directorRepository.save(director);
        return DirectorMapper.mapToDirectorDto(savedDirector);
    }

    @Override
    public DirectorDto getDirectorById(Long directorId) {
        Director director = findDirectorOrThrow(directorId);
        return DirectorMapper.mapToDirectorDto(director);
    }

    @Override
    public List<DirectorDto> getAllDirectors() {
        List<Director> directors = directorRepository.findAll();
        return directors.stream()
                .map(DirectorMapper::mapToDirectorDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DirectorDto> getDirectorsByProjectId(Long projectId) {
        // Ensure project exists before fetching
        resolveProject(projectId, true);

        List<Director> directors = directorRepository.findByProjectId(projectId);
        return directors.stream()
                .map(DirectorMapper::mapToDirectorDto)
                .collect(Collectors.toList());
    }

    @Override
    public DirectorDto updateDirector(Long directorId, DirectorDto updatedDirector) {
        validateRequiredFields(updatedDirector);

        Director director = findDirectorOrThrow(directorId);

        director.setDirName(updatedDirector.getDirName());
        director.setDirDesignation(updatedDirector.getDirDesignation());
        director.setContact(updatedDirector.getContact());
        director.setEmail(updatedDirector.getEmail());
        director.setAssignedDate(updatedDirector.getAssignedDate());
        director.setReleaseDate(updatedDirector.getReleaseDate());

        if (updatedDirector.getDutyRole() != null) {
            director.setDutyRole(Director.DutyRole.fromLabel(updatedDirector.getDutyRole()));
        }

        // Relational updates
        if (updatedDirector.getMinistryId() != null) {
            director.setMinistry(resolveMinistry(updatedDirector.getMinistryId(), true));
        }

        if (updatedDirector.getDirectorateId() != null) {
            director.setDirectorate(resolveDirectorate(updatedDirector.getDirectorateId(), false));
        }

        if (updatedDirector.getProjectId() != null) {
            director.setProject(resolveProject(updatedDirector.getProjectId(), true));
        }

        // Image handling:
        // - image == null  -> field wasn't sent, leave existing image untouched
        // - image == ""    -> user explicitly removed the image
        // - image == "..." -> new image string, replaces existing image
        if (updatedDirector.getImage() != null) {
            if (updatedDirector.getImage().isBlank()) {
                director.setImage(null);
            } else {
                director.setImage(updatedDirector.getImage());
            }
        }

        Director updatedDirectorObj = directorRepository.save(director);
        return DirectorMapper.mapToDirectorDto(updatedDirectorObj);
    }

    @Override
    public void deleteDirector(Long directorId) {
        findDirectorOrThrow(directorId);
        directorRepository.deleteById(directorId);
    }

    // ---- helpers ----

    private Director findDirectorOrThrow(Long directorId) {
        return directorRepository.findById(directorId)
                .orElseThrow(() -> new RuntimeException("Director not found with ID: " + directorId));
    }

    private Ministry resolveMinistry(Long ministryId, boolean required) {
        if (ministryId == null) {
            if (required) {
                throw new IllegalArgumentException("Ministry is required.");
            }
            return null;
        }
        return ministryRepository.findById(ministryId)
                .orElseThrow(() -> new RuntimeException("Ministry not found with ID: " + ministryId));
    }

    private Directorate resolveDirectorate(Long directorateId, boolean required) {
        if (directorateId == null) {
            if (required) {
                throw new IllegalArgumentException("Directorate is required.");
            }
            return null;
        }
        return directorateRepository.findById(directorateId)
                .orElseThrow(() -> new RuntimeException("Directorate not found with ID: " + directorateId));
    }

    private Project resolveProject(Long projectId, boolean required) {
        if (projectId == null) {
            if (required) {
                throw new IllegalArgumentException("Project is required.");
            }
            return null;
        }
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
    }

    /**
     * Enforces required business fields.
     */
    private void validateRequiredFields(DirectorDto dto) {
        List<String> missing = new ArrayList<>();

        if (isBlank(dto.getDirName())) missing.add("dirName");
        if (isBlank(dto.getDirDesignation())) missing.add("dirDesignation");
        if (isBlank(dto.getContact())) missing.add("contact");
        if (isBlank(dto.getEmail())) missing.add("email");
        if (dto.getMinistryId() == null) missing.add("ministryId");
        if (dto.getProjectId() == null) missing.add("projectId");
        if (dto.getAssignedDate() == null) missing.add("assignedDate");

        if (!missing.isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing required field(s): " + String.join(", ", missing));
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}