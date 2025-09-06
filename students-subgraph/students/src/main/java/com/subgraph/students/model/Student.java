package com.subgraph.students.model;

public record Student(String id, String text, Integer starRating) {

  public Student(String id, Integer starRating) {
    this(id, null, starRating);
  }
}
