package com.subgraph.university.service;

import com.subgraph.university.model.Campus;
import com.subgraph.university.model.Status;
import com.subgraph.university.model.University;
import com.subgraph.university.entity.UniversityEntity;
import com.subgraph.university.entity.CampusEntity;
import com.subgraph.university.model.UniversityInput;
import com.subgraph.university.repository.CampusRepository;
import com.subgraph.university.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository repository;

    @Autowired
    private CampusRepository campusRepository;

    public University getUniversity(Long id) {
        return repository.findById(id)
                .map(this::toUniversity)
                .orElse(null);
    }

    public List<University> getUniversities() {
        return repository.findAll().stream()
                .map(this::toUniversity)
                .collect(Collectors.toList());
    }

    public List<Object> searchUniversities(String name, Status status) {

        // creating the Union of University and Campus objects
        // Add University objects in result, whose name contains the 'name' parameter.
        List<Object> results = repository.findAll().stream()
                .filter(e -> name == null || e.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(e -> status == null || e.getStatus() == status)
                .map(this::toUniversity)
                .collect(Collectors.toList());

        // Add campuses that match the name filter that is, if campus name contains the 'name' parameter then include it.
        results.addAll(campusRepository.findAll().stream()
                .filter(c -> name == null || c.getName().toLowerCase().contains(name.toLowerCase()))
                .map(this::toCampus)
                .collect(Collectors.toList()));

        // finally, results contain University and Campus objects which have 'name' parameter as subset of their names.
        return results;
    }

    private Campus toCampus(CampusEntity e) {
        return new Campus(e.getId(), e.getName());
    }

    private University toUniversity(UniversityEntity e) {
        return new University(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getStatus() != null ? e.getStatus() : Status.ACTIVE,
                e.getCreatedAt() != null ? e.getCreatedAt() : LocalDate.now(),
                e.getRanking()
        );
    }

    public University createUniversity(String name, String description, String status, Integer ranking) {
        UniversityEntity entity = new UniversityEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setStatus(Status.valueOf(status));
        entity.setRanking(ranking);
        entity.setCreatedAt(LocalDate.now());
        entity = repository.save(entity);
        return new University(entity.getId(), entity.getName(), entity.getDescription(), entity.getStatus(), entity.getCreatedAt(), entity.getRanking());
    }

    public Campus createCampus(Long id, String name) {
        CampusEntity entity = new CampusEntity();
        entity.setId(id);
        entity.setName(name);
        entity = campusRepository.save(entity);
        return new Campus(entity.getId(), entity.getName());
    }
}