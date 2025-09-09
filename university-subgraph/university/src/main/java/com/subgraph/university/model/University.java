package com.subgraph.university.model;

import java.time.LocalDate;

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
}