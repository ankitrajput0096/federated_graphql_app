package com.graphql.classes.controller;


import com.graphql.classes.model.Class;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ClassController {
    // Mock data: Keyed by teacherId
    private final Map<String, List<Class>> classesByTeacher = Map.of(
            "1020", List.of(new Class("C1", "Math", "1020", LocalTime.of(1, 0)), new Class("C2", "Science", "1020", LocalTime.of(14, 30))),
            "1021", List.of(new Class("C3", "EVS", "1021", LocalTime.of(2, 55)), new Class("C4", "Sports", "1021", LocalTime.of(10, 0))),
            "1030", List.of(new Class("C5", "Arts", "1030", LocalTime.of(3, 33)), new Class("C6", "Politics", "1030", LocalTime.of(9, 1))),
            "1031", List.of(new Class("C7", "IP", "1031", LocalTime.of(4, 22)), new Class("C8", "CS", "1031", LocalTime.of(6, 42)))
            // Add more mock data as needed for testing
    );

    @QueryMapping
    public List<Class> classes(@Argument String teacherId) {
        return classesByTeacher.getOrDefault(teacherId, List.of());
    }

    @QueryMapping
    public List<Class> batchClasses(@Argument List<String> teacherIds) {
        return teacherIds.stream()
                .flatMap(id -> classesByTeacher.getOrDefault(id, List.of()).stream())
                .collect(Collectors.toList());
    }
}