package com.subgraph.teachers.model;

import java.util.ArrayList;
import java.util.List;

public record Teacher(String id, String text, int starRating, List<Class> classes, TeacherLevel level) {

  public Teacher(String id, Integer starRating) {
    this(id, null, starRating, null, null);
  }
}
