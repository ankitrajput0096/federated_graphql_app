package com.subgraph.university.controller;

import com.subgraph.university.model.Campus;
import com.subgraph.university.model.University;
import com.subgraph.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UniversityMutationController {

    @Autowired
    private UniversityService service;

    @MutationMapping
    public University createUniversity(
            @Argument String name,
            @Argument String description,
            @Argument String status,
            @Argument Integer ranking
    ) {
        // Validate inputs
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("University name is required");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("University status is required");
        }
        // Validate status is a valid enum value
        if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
            throw new IllegalArgumentException("Invalid status: must be ACTIVE or INACTIVE");
        }

        if (ranking != null && ranking < 0) {
            throw new IllegalArgumentException("Ranking cannot be negative");
        }

        return service.createUniversity(name, description, status, ranking);
    }

    @MutationMapping
    public Campus createCampus(
            @Argument Long id,
            @Argument String name
    ) {
        // Validate inputs
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Campus ID must be a positive number");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Campus name is required");
        }

        return service.createCampus(id, name);
    }
}