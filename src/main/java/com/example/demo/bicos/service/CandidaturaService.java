package com.example.demo.bicos.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bicos.controller.dto.CandidaturaPendenteDto;
import com.example.demo.bicos.controller.dto.CandidaturasDto;
import com.example.demo.bicos.models.Candidatura;
import com.example.demo.bicos.models.CandidaturaStatus;
import com.example.demo.bicos.models.HistAprovacao;
import com.example.demo.bicos.models.HistAprovacaoStatus;
import com.example.demo.bicos.models.Notification;
import com.example.demo.bicos.models.UserRole;
import com.example.demo.bicos.repo.BicosRepository;
import com.example.demo.bicos.repo.CandidaturaRepository;
import com.example.demo.bicos.repo.HistAprovacaoRepository;
import com.example.demo.bicos.repo.NotificationRepository;
import com.example.demo.bicos.repo.UserRepository;

import com.example.demo.bicos.controller.dto.HistAprovacaoDto;

@Service
public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepo;

    private final UserRepository userRepo;

    private final BicosRepository bicosRepo;

    private final HistAprovacaoRepository histAprovacaoRepo;

    private final NotificationRepository notificationRepo;

    public CandidaturaService(CandidaturaRepository candidaturaRepo, UserRepository userRepo, BicosRepository bicosRepo,
            HistAprovacaoRepository histAprovacaoRepo, NotificationRepository notificationRepo) {
        this.candidaturaRepo = candidaturaRepo;
        this.userRepo = userRepo;
        this.bicosRepo = bicosRepo;
        this.histAprovacaoRepo = histAprovacaoRepo;
        this.notificationRepo = notificationRepo;
    }

    public Long candidatar(Long bicoId, String userId) {

        var user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var bico = bicosRepo.findById(bicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (candidaturaRepo.existsByBicosAndUser(bico, user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já se candidatou a este bico.");
        }

        var candidatura = new Candidatura();
        candidatura.setUser(user);
        candidatura.setBicos(bico);
        candidatura.setStatus(CandidaturaStatus.PENDENTE);

        var candidaturaSalva = candidaturaRepo.save(candidatura);

        HistAprovacao hist = new HistAprovacao();
        hist.setUser(user);
        hist.setCandidatura(candidaturaSalva);
        hist.setDecisao(HistAprovacaoStatus.PENDENTE);
        hist.setMotivo("O candidato está no nível Pendente");
        histAprovacaoRepo.save(hist);

        notificationRepo
                .save(new Notification(user, "Candidatura realizada", "Você se candidatou ao bico: " + bico.getName()));

        notifyApproversByRole(UserRole.APROVADOR_N1,
                "Nova candidatura para aprovação",
                String.format("Uma nova candidatura para o bico '%s' está aguardando sua análise.", bico.getName()));

        return candidaturaSalva.getId();
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
                HistAprovacao hist = new HistAprovacao();
                hist.setUser(aprovador);
                hist.setCandidatura(candidatura);
                hist.setDecisao(HistAprovacaoStatus.AGUARDANDO_N2);
                hist.setMotivo("O candidato está no nível Aguardando N2");
                histAprovacaoRepo.save(hist);
                notifyApproversByRole(UserRole.APROVADOR_N2,
                        "Nova aprovação pendente",
                        String.format("A candidatura para o bico '%s' passou para sua análise.",
                                candidatura.getBicos().getName()));
            }
            case APROVADOR_N2 -> {
                if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N2) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Candidatura requer aprovação N1 primeiro");
                }
                candidatura.setStatus(CandidaturaStatus.AGUARDANDO_N3);
                HistAprovacao hist = new HistAprovacao();
                hist.setUser(aprovador);
                hist.setCandidatura(candidatura);
                hist.setDecisao(HistAprovacaoStatus.AGUARDANDO_N3);
                hist.setMotivo("O candidato está no nível Aguardando N3");
                histAprovacaoRepo.save(hist);
                notifyApproversByRole(UserRole.APROVADOR_N3,
                        "Nova aprovação pendente",
                        String.format("A candidatura para o bico '%s' passou para sua análise.",
                                candidatura.getBicos().getName()));
            }
            case APROVADOR_N3 -> {
                if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N3) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Candidatura requer aprovação N2 primeiro");
                }
                candidatura.setStatus(CandidaturaStatus.APROVADO);
                HistAprovacao hist = new HistAprovacao();
                hist.setUser(aprovador);
                hist.setCandidatura(candidatura);
                hist.setDecisao(HistAprovacaoStatus.APROVADO);
                hist.setMotivo("Aprovação final concluída pelo N3");
                histAprovacaoRepo.save(hist);
                Notification notification = new Notification();
                notification.setUser(candidatura.getUser());
                notification.setTitle("Candidatura Aprovada");
                notification.setDescription("Sua candidatura foi aprovada.");
                notificationRepo.save(notification);
            }
            default ->
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para aprovar");
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
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "N1 não pode rejeitar após aprovação do N2 ou superior");
                }
            }
            case APROVADOR_N2 -> {
                if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N2) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "N2 não pode rejeitar após aprovação do N3");
                }
            }
            case APROVADOR_N3 -> {
                if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N3) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "N3 só pode rejeitar na sua etapa");
                }
            }
            default ->
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para rejeitar");
        }

        candidatura.setStatus(CandidaturaStatus.REJEITADO);
        candidaturaRepo.save(candidatura);

        HistAprovacao historico = new HistAprovacao();
        historico.setCandidatura(candidatura);
        historico.setUser(aprovador);
        historico.setDecisao(HistAprovacaoStatus.REJEITADO);
        historico.setMotivo("Rejeitado pelo " + aprovador.getRole() + " devido ao seguinte motivo: " + motivo);

        histAprovacaoRepo.save(historico);

        notificationRepo.save(new Notification(candidatura.getUser(), "Candidatura Rejeitada",
                "Sua candidatura foi rejeitada pelo " + aprovador.getRole() + ". Motivo: " + motivo));
    }

    public Page<CandidaturasDto> minhasCandidaturasPaginadas(String userId, CandidaturaStatus status, int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        UUID candidato = UUID.fromString(userId);

        Page<Candidatura> candidaturas;

        if (status != null) {
            candidaturas = candidaturaRepo.findByUserIdAndStatus(candidato, status, pageable);
        } else {
            candidaturas = candidaturaRepo.findByUserId(candidato, pageable);
        }

        return candidaturas.map(c -> new CandidaturasDto(
                c.getId(),
                c.getStatus(),
                c.getDataSolicitacao(),
                c.getBicos().getName()));
    }

    public Page<CandidaturaPendenteDto> nivelPendentePaginado(String aprovadorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        var aprovador = userRepo.findById(UUID.fromString(aprovadorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aprovador não encontrado"));

        CandidaturaStatus statusAlvo = switch (aprovador.getRole()) {
            case APROVADOR_N1 -> CandidaturaStatus.PENDENTE;
            case APROVADOR_N2 -> CandidaturaStatus.AGUARDANDO_N2;
            case APROVADOR_N3 -> CandidaturaStatus.AGUARDANDO_N3;
            default -> throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não possui papel de aprovador");
        };

        return candidaturaRepo.findAllByStatus(statusAlvo, pageable).map(c -> new CandidaturaPendenteDto(
                c.getId(),
                c.getStatus(),
                c.getUser().getLogin(),
                c.getBicos().getName(),
                c.getBicos().getBicosFilter().getFilter(),
                c.getBicos().getPrice()));
    }

    private void notifyApproversByRole(UserRole role, String title, String description) {
        userRepo.findAll().stream()
                .filter(user -> role.equals(user.getRole()))
                .forEach(user -> notificationRepo.save(new Notification(user, title, description)));
    }

    public void devolver(Long candidaturaId, String aprovadorId, String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Motivo obrigatório para devolução");
        }

        var candidatura = candidaturaRepo.findById(candidaturaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var aprovador = userRepo.findById(UUID.fromString(aprovadorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        switch (aprovador.getRole()) {
            case APROVADOR_N1 -> {
                if (candidatura.getStatus() != CandidaturaStatus.PENDENTE) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "N1 só pode devolver candidaturas no status PENDENTE");
                }
                candidatura.setStatus(CandidaturaStatus.DEVOLVIDO);
                HistAprovacao hist = new HistAprovacao();
                hist.setUser(aprovador);
                hist.setCandidatura(candidatura);
                hist.setDecisao(HistAprovacaoStatus.DEVOLVIDO);
                hist.setMotivo(motivo);
                histAprovacaoRepo.save(hist);

                Notification notification = new Notification();
                notification.setUser(candidatura.getUser());
                notification.setTitle("Candidatura Devolvida");
                notification.setDescription("Sua candidatura foi devolvida pelo avaliador. Motivo: " + motivo);
                notificationRepo.save(notification);
            }
            case APROVADOR_N2 -> {
                if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N2) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "N2 só pode devolver candidaturas no status AGUARDANDO_N2");
                }
                candidatura.setStatus(CandidaturaStatus.PENDENTE);
                HistAprovacao hist = new HistAprovacao();
                hist.setUser(aprovador);
                hist.setCandidatura(candidatura);
                hist.setDecisao(HistAprovacaoStatus.PENDENTE);
                hist.setMotivo(motivo);
                histAprovacaoRepo.save(hist);
                notifyApproversByRole(UserRole.APROVADOR_N2,
                        "Candidatura devolvida pelo N3",
                        String.format("O bico '%s' voltou para o nível N2. Motivo: %s",
                                candidatura.getBicos().getName(), motivo));
            }
            case APROVADOR_N3 -> {
                if (candidatura.getStatus() != CandidaturaStatus.AGUARDANDO_N3) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "N3 só pode devolver candidaturas no status AGUARDANDO_N3");
                }
                candidatura.setStatus(CandidaturaStatus.AGUARDANDO_N2);
                HistAprovacao hist = new HistAprovacao();
                hist.setUser(aprovador);
                hist.setCandidatura(candidatura);
                hist.setDecisao(HistAprovacaoStatus.AGUARDANDO_N2);
                hist.setMotivo(motivo);
                histAprovacaoRepo.save(hist);
                notifyApproversByRole(UserRole.APROVADOR_N2,
                        "Candidatura devolvida pelo N3",
                        String.format("O bico '%s' voltou para o nível N2. Motivo: %s",
                                candidatura.getBicos().getName(), motivo));
            }
            default ->
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para devolver");
        }
        candidaturaRepo.save(candidatura);
    }

    public Long candidatarDevolvido(Long candidaturaId, String userId) {

        var candidatura = candidaturaRepo.findById(candidaturaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!candidatura.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Só o dono da candidatura pode reenviar");
        }

        if (candidatura.getStatus() != CandidaturaStatus.DEVOLVIDO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Candidatura não está devolvida");
        }

        candidatura.setStatus(CandidaturaStatus.PENDENTE);
        HistAprovacao hist = new HistAprovacao();
        hist.setUser(user);
        hist.setCandidatura(candidatura);
        hist.setDecisao(HistAprovacaoStatus.PENDENTE);
        hist.setMotivo("Candidatura reenviada");
        histAprovacaoRepo.save(hist);

        notificationRepo
                .save(new Notification(user, "Candidatura reenviada",
                        "Sua candidatura foi reenviada para análise no bico: " + candidatura.getBicos().getName()));

        notifyApproversByRole(UserRole.APROVADOR_N1,
                "Nova candidatura para aprovação",
                String.format("Uma candidatura para o bico '%s' foi reenviada e está aguardando análise.",
                        candidatura.getBicos().getName()));

        return candidaturaRepo.save(candidatura).getId();
    }

    public Page<HistAprovacaoDto> meuHistoricoPaginado(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        UUID candidato = UUID.fromString(userId);

        return histAprovacaoRepo.findByUserId(candidato, pageable).map(c -> new HistAprovacaoDto(
                c.getId(),
                c.getDecisao(),
                c.getMotivo(),
                c.getDataAprovacao()));
    }

}
