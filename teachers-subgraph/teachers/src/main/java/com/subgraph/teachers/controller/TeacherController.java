package com.subgraph.teachers.controller;

import com.subgraph.teachers.model.Class;
import com.subgraph.teachers.model.Teacher;
import com.subgraph.teachers.model.TeacherSource;
import com.subgraph.teachers.model.University;
import com.subgraph.teachers.service.ClassFetcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.federation.EntityMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
public class TeacherController {

    private final ClassFetcherService classFetcherService;

    public TeacherController(ClassFetcherService classFetcherService) {
        this.classFetcherService = classFetcherService;
    }

    @EntityMapping
    public University university(@Argument Long id) {
        return new University(id);
    }

    @SchemaMapping
    public CompletableFuture<List<Teacher>> teacher(University university) {
        return CompletableFuture.supplyAsync(() -> TeacherSource.getTeacher(university));
    }

    @SchemaMapping
    public CompletableFuture<List<Class>> classes(Teacher teacher) {
        log.info("Fetching classes for teacher ID: {}", teacher.id());
        return classFetcherService.fetchClasses(teacher.id()).toFuture();
    }

    @QueryMapping
    public CompletableFuture<List<Teacher>> teacher(@Argument Long universityId) {
        log.info("Querying teachers for universityId: {}", universityId);
        return CompletableFuture.supplyAsync(() -> TeacherSource.getTeacher(new University(universityId)));
    }

    @QueryMapping
    public List<Teacher> allTeachers() {
        return TeacherSource.getAllTeachers();
    }
}