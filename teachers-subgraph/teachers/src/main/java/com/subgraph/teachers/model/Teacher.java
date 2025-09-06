package com.subgraph.teachers.model;

public record Teacher(String id, String text, Integer starRating) {

  public Teacher(String id, Integer starRating) {
    this(id, null, starRating);
  }
}
