package com.subgraph.teachers.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TeacherSource {

  private static final Map<Long, List<Teacher>> teacherMap = Map.of(
    2L, List.of(new Teacher("1020", "Very cramped :( Do not recommend.", 2, null), new Teacher("1021", "Got me to the Moon!", 4, null)),
    3L, List.of(new Teacher("1030", 3)),
    4L, List.of(new Teacher("1040", 5), new Teacher("1041", "Reusable!", 5, null), new Teacher("1042", 5)),
    5L, List.of(new Teacher("1050", "Amazing! Would Fly Again!", 5, null), new Teacher("1051", 5))
  );

  public static List<Teacher> getTeacher(University university) {
    return teacherMap.getOrDefault(university.id(), Collections.emptyList());
  }

  public static List<Teacher> getAllTeachers() {
    return teacherMap.values().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }
}
