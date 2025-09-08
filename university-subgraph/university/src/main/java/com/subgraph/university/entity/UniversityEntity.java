package com.subgraph.university.entity;

import com.subgraph.university.model.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "universities")
@Getter
@Setter
public class UniversityEntity {
    @Id
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate createdAt;
    private Integer ranking;

    public UniversityEntity() {}

    public UniversityEntity(Long id, String name, String description, Status status, LocalDate createdAt, Integer ranking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.ranking = ranking;
    }
}