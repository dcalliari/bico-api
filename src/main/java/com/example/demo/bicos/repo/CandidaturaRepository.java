package com.example.demo.bicos.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.bicos.controller.dto.CandidaturasDto;
import com.example.demo.bicos.models.Bicos;
import com.example.demo.bicos.models.Candidatura;
import com.example.demo.bicos.models.CandidaturaStatus;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {
    boolean existsByBicos(Bicos bicos);
    
    List<CandidaturasDto> findByUserId(UUID userId);

    List<CandidaturasDto> findByStatus(CandidaturaStatus status);

    Page<Candidatura> findAllByStatus(CandidaturaStatus status, Pageable pageable); 

    Page<Candidatura> findByUserId(UUID userId, Pageable pageable);
}
