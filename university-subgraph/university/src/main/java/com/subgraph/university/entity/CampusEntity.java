package com.subgraph.university.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "campus")
@Getter
@Setter
public class CampusEntity {
    @Id
    private Long id;
    private String name;

    public CampusEntity() {}

    public CampusEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}