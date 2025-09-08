package com.subgraph.university.repository;

import com.subgraph.university.entity.CampusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<CampusEntity, Long> {}
