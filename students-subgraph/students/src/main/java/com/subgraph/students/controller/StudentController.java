package com.subgraph.students.controller;

import com.subgraph.students.model.*;
import com.subgraph.students.service.GradeFetcherService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.federation.EntityMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
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
    public CompletableFuture<Grade> grade(Student student) {
        log.info("Fetching grade for student {} using GradeFetcherService", student.id());
        return CompletableFuture.supplyAsync(() -> gradeFetcherService.fetchGrade(student.id()));
    }

    @SchemaMapping(typeName = "Grade", field = "rating")
    public int rating(Grade grade, @Argument Integer filter) {
        log.info("Computing rating for grade {}, filter {}", grade.studentId(), filter);
        int rating = (int) (grade.gpa() * 2);
        if (filter != null && rating < filter) {
            return 0;
        }
        return rating;
    }

    @QueryMapping
    public List<Student> student(@Argument Long universityId) {
        log.info("Querying students for universityId: {}", universityId);
        return StudentSource.getStudent(new University(universityId));
    }

    @QueryMapping
    public List<Student> allStudents() {
        return StudentSource.getAllStudents();
    }

    @MutationMapping
    public Student addStudent(@Argument StudentInput input) {
        log.info("Adding student with id: {} for universityId: {}", input.getId(), input.getUniversityId());
        Grade grade = input.getGrade() != null
                ? new Grade(input.getGrade().getStudentId(), input.getGrade().getGrade(), input.getGrade().getGpa())
                : gradeFetcherService.fetchGrade(input.getId());
        if (grade == null) {
            grade = new Grade(input.getId(), "N/A", 0.0f);
        }
        Student student = new Student(input.getId(), input.getText(), input.getStarRating(), grade, input.getStatus());
        StudentSource.addStudent(input.getUniversityId(), student);
        return student;
    }

    @MutationMapping
    public Student updateStudent(@Argument StudentInput input) {
        log.info("Updating student with id: {} for universityId: {}", input.getId(), input.getUniversityId());
        Grade grade = input.getGrade() != null
                ? new Grade(input.getGrade().getStudentId(), input.getGrade().getGrade(), input.getGrade().getGpa())
                : null;
        Student updatedStudent = StudentSource.updateStudent(
                input.getUniversityId(),
                input.getId(),
                input.getText(),
                input.getStarRating(),
                input.getStatus(),
                grade
        );
        if (updatedStudent == null) {
            log.error("Student with id {} not found in university {}", input.getId(), input.getUniversityId());
            throw new RuntimeException("Student not found");
        }
        return updatedStudent;
    }

    @Data
    @NoArgsConstructor
    public static class StudentInput {
        private String id;
        private String text;
        private int starRating;
        private Long universityId;
        private GradeLevel status = GradeLevel.C;
        private GradeInput grade;
    }

    @Data
    @NoArgsConstructor
    public static class GradeInput {
        private String studentId;
        private String grade;
        private float gpa;
    }
}