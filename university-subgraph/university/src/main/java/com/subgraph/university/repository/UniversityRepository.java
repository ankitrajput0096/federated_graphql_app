package com.subgraph.university.repository;

import com.subgraph.university.entity.UniversityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<UniversityEntity, Long> {}