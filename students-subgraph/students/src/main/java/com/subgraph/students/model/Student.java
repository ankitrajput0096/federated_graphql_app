package com.subgraph.students.model;

import com.subgraph.students.model.GradeLevel;

public record Student(String id, String text, int starRating, Grade grade, GradeLevel status) {
  // Constructor for backward compatibility with existing data
  public Student(String id, String text, int starRating) {
    this(id, text, starRating, new Grade(id, "N/A", 0.0f), GradeLevel.C);
  }

  // Constructor for single starRating (used in StudentSource for id=1030)
  public Student(String id, int starRating) {
    this(id, null, starRating, new Grade(id, "N/A", 0.0f), GradeLevel.C);
  }
}
