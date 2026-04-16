package com.example.demo.bicos.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.bicos.models.Bicos;
import com.example.demo.bicos.models.Candidatura;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {
    boolean existsByBicos(Bicos bicos);
}
