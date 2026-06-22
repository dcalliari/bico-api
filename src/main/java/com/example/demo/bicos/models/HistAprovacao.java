package com.example.demo.bicos.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="hist_aprov")
public class HistAprovacao {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "candidatura_id")
    private Candidatura candidatura;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="decisao")
    private HistAprovacaoStatus decisao;

    @Column(name="motivo")
    private String motivo;

    @CreationTimestamp
    @Column(name="data_aprovacao")
    private LocalDateTime dataAprovacao;

    public HistAprovacao() {
    }

    public HistAprovacao(Long id, Candidatura candidatura, User user, HistAprovacaoStatus decisao, String motivo,
            LocalDateTime dataAprovacao) {
        this.id = id;
        this.candidatura = candidatura;
        this.user = user;
        this.decisao = decisao;
        this.motivo = motivo;
        this.dataAprovacao = dataAprovacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidatura getCandidatura() {
        return candidatura;
    }

    public void setCandidatura(Candidatura candidatura) {
        this.candidatura = candidatura;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HistAprovacaoStatus getDecisao() {
        return decisao;
    }

    public void setDecisao(HistAprovacaoStatus decisao) {
        this.decisao = decisao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    
}
