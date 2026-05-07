package com.example.demo.bicos.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.bicos.models.Bicos;

public interface BicosRepository extends JpaRepository<Bicos, Long> {
    Page<Bicos> findByCityIgnoreCase(String city, Pageable pageable);
}
