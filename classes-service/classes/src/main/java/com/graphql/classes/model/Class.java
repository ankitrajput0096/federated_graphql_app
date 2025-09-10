package com.graphql.classes.model;


import java.time.LocalTime;

public record Class(String id, String subject, String teacherId, LocalTime duration) {
    public Class {
        if (duration == null) {
            throw new IllegalArgumentException("Duration cannot be null");
        }
    }
}