package com.subgraph.university.controller;


import com.subgraph.university.model.University;
import com.subgraph.university.model.UniversitySource;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UniversityController {

    @QueryMapping
    public University university(@Argument Long id) {
        return UniversitySource.getUniversity(id);
    }

    @QueryMapping
    public List<University> universities() {
        return UniversitySource.getUniversities();
    }
}