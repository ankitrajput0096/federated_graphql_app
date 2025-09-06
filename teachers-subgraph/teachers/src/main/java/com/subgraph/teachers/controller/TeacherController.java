package com.subgraph.teachers.controller;

import com.subgraph.teachers.model.Teacher;
import com.subgraph.teachers.model.TeacherSource;
import com.subgraph.teachers.model.University;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.federation.EntityMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
public class TeacherController {

    @EntityMapping
    public University university(@Argument Long id) {
        return new University(id);
    }

    @SchemaMapping
    public List<Teacher> teacher(University university) {
        return TeacherSource.getTeacher(university);
    }

    @QueryMapping
    public List<Teacher> teacher(@Argument Long universityId) {
        log.info("the parameter is "+universityId);
        return TeacherSource.getTeacher(new University(universityId));
    }

    @QueryMapping
    public List<Teacher> allTeachers() {
        return TeacherSource.getAllTeachers();
    }
}