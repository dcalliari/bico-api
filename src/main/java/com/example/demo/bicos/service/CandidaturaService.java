package com.example.demo.bicos.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bicos.models.Candidatura;
import com.example.demo.bicos.models.CandidaturaStatus;
import com.example.demo.bicos.models.HistAprovacao;
import com.example.demo.bicos.models.HistAprovacaoStatus;
import com.example.demo.bicos.repo.BicosRepository;
import com.example.demo.bicos.repo.CandidaturaRepository;
import com.example.demo.bicos.repo.HistAprovacaoRepository;
import com.example.demo.bicos.repo.UserRepository;

@Service
public class CandidaturaService {

    @Autowired
    private CandidaturaRepository candidaturaRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BicosRepository bicosRepo;

    @Autowired
    private HistAprovacaoRepository histAprovacaoRepo;

    public Long candidatar(Long bicoId, String userId) {

        var user = userRepo.findById(UUID.fromString(userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var bico = bicosRepo.findById(bicoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (candidaturaRepo.existsByBicos(bico)) {
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

        var candidatura = candidaturaRepo.findById(candidaturaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        switch (aprovador.getRole()) {
        case APROVADOR_N1 -> {
            if (candidatura.getStatus() != CandidaturaStatus.PENDENTE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidatura não está pendente");
            }
            candidatura.setStatus(CandidaturaStatus.AGUARDANDO_N2);
        }
        case APROVADOR_N2 -> {
            if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidatura requer aprovação N1 primeiro");
            }
            candidatura.setStatus(CandidaturaStatus.AGUARDANDO_N3);
        }
        case APROVADOR_N3 -> {
            if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N3) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidatura requer aprovação N2 primeiro");
            }
            candidatura.setStatus(CandidaturaStatus.APROVADO); 
            HistAprovacao hist = new HistAprovacao();
            hist.setUser(aprovador);
            hist.setCandidatura(candidatura);
            hist.setDecisao(HistAprovacaoStatus.APROVADO);
            hist.setMotivo("Aprovação final concluída pelo N3");
            histAprovacaoRepo.save(hist);
        }
        }

        candidaturaRepo.save(candidatura);
    }

    
    public void rejeitar(Long candidaturaId, String aprovadorId, String motivo) {

        var candidatura = candidaturaRepo.findById(candidaturaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        var aprovador = userRepo.findById(UUID.fromString(aprovadorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        switch (aprovador.getRole()) {
        case APROVADOR_N1 -> {
            if (candidatura.getStatus() != CandidaturaStatus.PENDENTE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "N1 não pode rejeitar após aprovação do N2 ou superior");
            }
        }
        case APROVADOR_N2 -> {
            if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "N2 não pode rejeitar após aprovação do N3");
            }
        }
        case APROVADOR_N3 -> {
            if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N3) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "N3 só pode rejeitar na sua etapa");
            }
        }
        default -> throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para rejeitar");
    }

    candidatura.setStatus(CandidaturaStatus.REJEITADO);
    candidaturaRepo.save(candidatura);

    HistAprovacao historico = new HistAprovacao();
    historico.setCandidatura(candidatura);
    historico.setUser(aprovador);
    historico.setDecisao(HistAprovacaoStatus.REJEITADO);
    historico.setMotivo("Rejeitado pelo "+ aprovador.getRole()+" devido ao seguinte motivo: "+ motivo);
    

    histAprovacaoRepo.save(historico);
}
    

    
}
