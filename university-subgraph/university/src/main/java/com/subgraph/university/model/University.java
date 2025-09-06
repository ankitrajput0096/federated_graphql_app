package com.subgraph.university.model;

/**
 * type University @key(fields: "id") {
 *     id: ID!
 *     name: String!
 *     description: String
 * }
 */
public record University(Long id, String name, String description) {

    public University(Long id, String name) {
        this(id, name, null);
    }
}