package com.student.grades;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grades")
public class GradeController {
    private final Map<String, Grade> grades = Map.of(
            "1020", new Grade("1020", "A", 4.0f),
            "1021", new Grade("1021", "B", 3.0f), // Add more mock data if needed
            "1031", new Grade("1031", "B", 3.0f), // Add more mock data if needed
            "1032", new Grade("1032", "B", 3.0f) // Add more mock data if needed
    );

    @GetMapping("/{studentId}")
    public Grade getGrade(@PathVariable String studentId) {
        return grades.get(studentId);
    }

    @PostMapping("/batch")
    public List<Grade> getBatchGrades(@RequestBody List<String> studentIds) {
        return studentIds.stream()
                .map(grades::get)
                .filter(grade -> grade != null) // Handle missing grades gracefully
                .collect(Collectors.toList());
    }
}
