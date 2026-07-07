package com.example.demo.bicos.repo;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.bicos.models.HistAprovacao;

public interface HistAprovacaoRepository extends JpaRepository<HistAprovacao, Long> {
    Page<HistAprovacao> findByUserId(UUID userId, Pageable pageable);
}
