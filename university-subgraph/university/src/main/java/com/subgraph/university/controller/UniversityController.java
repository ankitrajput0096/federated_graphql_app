package com.subgraph.university.controller;


import com.subgraph.university.model.University;
import com.subgraph.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UniversityController {

    @Autowired
    private UniversityService service;

    @QueryMapping
    public University university(@Argument Long id) {
        return service.getUniversity(id);
    }

    @QueryMapping
    public List<University> universities() {
        return service.getUniversities();
    }
}