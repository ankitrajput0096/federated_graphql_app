package com.student.grades;

/**
 * NOTE: we are using record and not class because of following reasons:
 * 1. Reduced Boilerplate: A record automatically generates getters, constructor, equals(), hashCode(), and toString(),
 * so Grade(String studentId, String grade, float gpa) is one line versus a verbose class with manual implementations.
 * 2. Immutability by Default: Records are inherently immutable, ensuring Grade data (e.g., studentId, grade, gpa)
 * can’t be modified, which is ideal for a DTO in REST and GraphQL, unlike a class where immutability requires extra effort.
 */
public record Grade(String studentId, String grade, float gpa) {}

