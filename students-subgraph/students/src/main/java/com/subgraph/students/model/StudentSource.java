package com.subgraph.students.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class StudentSource {

  private static final Map<Long, List<Student>> studentMap = Map.of(
    2L, List.of(new Student("1020", "Very cramped :( Do not recommend.", 2), new Student("1021", "Got me to the Moon!", 4)),
    3L, List.of(new Student("1030", 3)),
    4L, List.of(new Student("1040", 5), new Student("1041", "Reusable!", 5), new Student("1042", 5)),
    5L, List.of(new Student("1050", "Amazing! Would Fly Again!", 5), new Student("1051", 5))
  );

  public static List<Student> getStudent(University university) {
    return studentMap.getOrDefault(university.id(), Collections.emptyList());
  }

  public static List<Student> getAllStudents() {
    return studentMap.values().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

}
