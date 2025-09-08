package com.subgraph.university.model;

import java.time.LocalDate;
import java.util.List;

/**
 * type University @key(fields: "id") {
 *     id: ID!
 *     name: String!
 *     description: String
 *     status: Status!
 *     createdAt: Date!
 *     ranking: Int
 * }
 */
public record University(
        Long id,
        String name,
        String description,
        Status status,
        LocalDate createdAt,
        Integer ranking
) {
    public University(Long id, String name) {
        this(id, name, null, Status.ACTIVE, LocalDate.now(), null);
    }

    // Resolver for name(filter: String): String! to satisfy Entity interface
    public String name(String filter) {
        if (filter == null || name == null) {
            return name;
        }
        return name.contains(filter) ? name : null;
    }
}