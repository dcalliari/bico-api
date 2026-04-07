package com.example.demo.bicos.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bicos.models.Candidatura;
import com.example.demo.bicos.models.CandidaturaStatus;
import com.example.demo.bicos.models.UserRole;
import com.example.demo.bicos.repo.BicosRepository;
import com.example.demo.bicos.repo.CandidaturaRepository;
import com.example.demo.bicos.repo.UserRepository;

@Service
public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepo;
    private final UserRepository userRepo;
    private final BicosRepository bicosRepo;

    public CandidaturaService(
        CandidaturaRepository candidaturaRepo,
        UserRepository userRepo,
        BicosRepository bicosRepo
    ) {
        this.candidaturaRepo = candidaturaRepo;
        this.userRepo = userRepo;
        this.bicosRepo = bicosRepo;
    }

    public Long candidatar(Long bicoId, String userId) {

        var user = userRepo.findById(UUID.fromString(userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user.getRole() != UserRole.FREELANCER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Só FREELANCER pode se candidatar");
        }

        var bico = bicosRepo.findById(bicoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (candidaturaRepo.existsByUserAndBicos(user, bico)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já se candidatou");
        }

        var candidatura = new Candidatura();
        candidatura.setUser(user);
        candidatura.setBicos(bico);
        candidatura.setStatus(CandidaturaStatus.PENDENTE);

        return candidaturaRepo.save(candidatura).getId();
    }

    public void aprovar(Long candidaturaId, String aprovadorId) {

        var aprovador = userRepo.findById(UUID.fromString(aprovadorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (aprovador.getRole() == UserRole.FREELANCER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        var candidatura = candidaturaRepo.findById(candidaturaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        candidatura.setStatus(CandidaturaStatus.APROVADO);

        candidaturaRepo.save(candidatura);
    }

    public void rejeitar(Long candidaturaId, String aprovadorId) {

        var aprovador = userRepo.findById(UUID.fromString(aprovadorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (aprovador.getRole() == UserRole.FREELANCER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        var candidatura = candidaturaRepo.findById(candidaturaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        candidatura.setStatus(CandidaturaStatus.REJEITADO);

        candidaturaRepo.save(candidatura);
    }
    

    
}
