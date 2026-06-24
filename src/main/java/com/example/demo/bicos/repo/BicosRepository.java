package com.example.demo.bicos.repo;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.bicos.models.Bicos;

public interface BicosRepository extends JpaRepository<Bicos, Long> {
    Page<Bicos> findByCidadeId(Long cidadeId, Pageable pageable);
    Page<Bicos> findByUserId(UUID userId, Pageable pageable);
}
