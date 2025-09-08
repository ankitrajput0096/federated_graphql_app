package com.subgraph.university.model;

public record Campus(Long id, String name) {
    // Resolver for name(filter: String): String! to satisfy Entity interface
    public String name(String filter) {
        if (filter == null || name == null) {
            return name;
        }
        return name.contains(filter) ? name : null;
    }
}