package com.subgraph.university.service;

import com.subgraph.university.model.University;
import com.subgraph.university.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository repository;

    public University getUniversity(Long id) {
        return repository.findById(id)
                .map(e -> new University(e.getId(), e.getName(), e.getDescription()))
                .orElse(null);
    }

    public List<University> getUniversities() {
        return repository.findAll().stream()
                .map(e -> new University(e.getId(), e.getName(), e.getDescription()))
                .collect(Collectors.toList());
    }

}
