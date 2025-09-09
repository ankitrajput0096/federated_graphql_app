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
import com.subgraph.students.model.Grade;
import com.subgraph.students.service.GradeFetcherService;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
public class StudentController {

    private final GradeFetcherService gradeFetcherService;

    public StudentController(GradeFetcherService gradeFetcherService) {
        this.gradeFetcherService = gradeFetcherService;
    }

    @EntityMapping
    public University university(@Argument Long id) {
        return new University(id);
    }

    @SchemaMapping
    public CompletableFuture<List<Student>> student(University university) {
        return CompletableFuture.supplyAsync(() -> StudentSource.getStudent(university));
    }

    @SchemaMapping(typeName = "Student", field = "grade")
    public Grade grade(Student student) {
        log.info("Fetching grade for student {}", student.id());
        return gradeFetcherService.fetchGrade(student.id());
    }

    @SchemaMapping(typeName = "Grade", field = "rating")
    public int rating(Grade grade, @Argument Integer filter) {
        log.info("Computing rating for grade {}, filter {}", grade.studentId(), filter);
        int rating = (int) (grade.gpa() * 2); // Example: GPA 4.0 -> rating 8
        if (filter != null && rating < filter) {
            return 0;
        }
        return rating;
    }

    @QueryMapping
    public List<Student> student(@Argument Long universityId) {
        log.info("the parameter is {}", universityId);
        return StudentSource.getStudent(new University(universityId));
    }

    @QueryMapping
    public List<Student> allStudents() {
        return StudentSource.getAllStudents();
    }
}