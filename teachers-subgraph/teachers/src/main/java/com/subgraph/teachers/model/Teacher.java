package com.subgraph.teachers.model;

import java.util.ArrayList;
import java.util.List;

public record Teacher(String id, String text, Integer starRating, List<Class> classes) {

  public Teacher(String id, Integer starRating) {
    this(id, null, starRating, null);
  }
}
