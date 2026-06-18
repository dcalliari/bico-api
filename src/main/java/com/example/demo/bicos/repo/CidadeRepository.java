package com.example.demo.bicos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.bicos.models.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {
}
