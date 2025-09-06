package com.subgraph.students.controller;

import com.subgraph.students.model.Student;
import com.subgraph.students.model.StudentSource;
import com.subgraph.students.model.University;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.federation.EntityMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
public class StudentController {

    @EntityMapping
    public University university(@Argument Long id) {
        return new University(id);
    }

    @SchemaMapping
    public List<Student> student(University university) {
        return StudentSource.getStudent(university);
    }

    @QueryMapping
    public List<Student> student(@Argument Long universityId) {
        log.info("the parameter is "+universityId);
        return StudentSource.getStudent(new University(universityId));
    }

    @QueryMapping
    public List<Student> allStudents() {
        return StudentSource.getAllStudents();
    }
}