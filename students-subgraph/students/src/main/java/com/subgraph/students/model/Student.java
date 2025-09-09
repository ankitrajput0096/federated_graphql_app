package com.subgraph.students.model;

public record Student(String id, String text, int starRating, Grade grade) {
  // Constructor for backward compatibility with existing data
  public Student(String id, String text, int starRating) {
    this(id, text, starRating, null);
  }

  // Constructor for single starRating (used in StudentSource for id=1030)
  public Student(String id, int starRating) {
    this(id, null, starRating, null);
  }
}
