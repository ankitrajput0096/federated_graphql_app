package com.subgraph.students.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class StudentSource {

  private static final Map<Long, List<Student>> studentMap = new ConcurrentHashMap<>(Map.of(
          2L, new ArrayList<>(List.of(
                  new Student("1020", "Very cramped :( Do not recommend.", 2, new Grade("1020", "C", 2.0f), GradeLevel.C),
                  new Student("1021", "Got me to the Moon!", 4, new Grade("1021", "A", 4.0f), GradeLevel.A)
          )),
          3L, new ArrayList<>(List.of(new Student("1030", null, 3, new Grade("1030", "B", 3.0f), GradeLevel.B))),
          4L, new ArrayList<>(List.of(
                  new Student("1040", null, 5, new Grade("1040", "A", 4.0f), GradeLevel.A),
                  new Student("1041", "Reusable!", 5, new Grade("1041", "A", 4.0f), GradeLevel.A),
                  new Student("1042", null, 5, new Grade("1042", "A", 4.0f), GradeLevel.A)
          )),
          5L, new ArrayList<>(List.of(
                  new Student("1050", "Amazing! Would Fly Again!", 5, new Grade("1050", "A", 4.0f), GradeLevel.A),
                  new Student("1051", null, 5, new Grade("1051", "A", 4.0f), GradeLevel.A)
          ))
  ));

  public static List<Student> getStudent(University university) {
    return studentMap.getOrDefault(university.id(), Collections.emptyList());
  }

  public static List<Student> getAllStudents() {
    return studentMap.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
  }

  public static void addStudent(Long universityId, Student student) {
    studentMap.computeIfAbsent(universityId, k -> new ArrayList<>()).add(student);
  }

  public static Student updateStudent(Long universityId, String studentId, String text, Integer starRating, GradeLevel status, Grade grade) {
    List<Student> students = studentMap.getOrDefault(universityId, Collections.emptyList());
    for (int i = 0; i < students.size(); i++) {
      Student student = students.get(i);
      if (student.id().equals(studentId)) {
        Grade updatedGrade = grade != null ? grade : student.grade();
        Student updatedStudent = new Student(
                studentId,
                text != null ? text : student.text(),
                starRating != null ? starRating : student.starRating(),
                updatedGrade,
                status != null ? status : student.status()
        );
        students.set(i, updatedStudent);
        return updatedStudent;
      }
    }
    return null; // Student not found
  }

  public static Student getStudentById(String studentId) {
    return getAllStudents().stream()
            .filter(student -> student.id().equals(studentId))
            .findFirst()
            .orElse(null);
  }
}
