package com.example.demo.bicos.models;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="candidatura")
public class Candidatura {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToOne
    @JoinColumn(name = "bicos_id")
    private Bicos bicos;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private CandidaturaStatus status;

    @CreationTimestamp
    @Column(name="data_solicitacao")
    private Instant dataSolicitacao;

    @JsonIgnore
    @OneToMany(mappedBy = "candidatura")
    private List<HistAprovacao> histAprovacao;

    public Candidatura() {
    }

    public Candidatura(Long id, User user, Bicos bicos, CandidaturaStatus status, Instant dataSolicitacao) {
        this.id = id;
        this.user = user;
        this.bicos = bicos;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bicos getBicos() {
        return bicos;
    }

    public void setBicos(Bicos bicos) {
        this.bicos = bicos;
    }

    public CandidaturaStatus getStatus() {
        return status;
    }

    public void setStatus(CandidaturaStatus status) {
        this.status = status;
    }

    public Instant getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Instant dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public List<HistAprovacao> getHistAprovacao() {
        return histAprovacao;
    }

    public void setHistAprovacao(List<HistAprovacao> histAprovacao) {
        this.histAprovacao = histAprovacao;
    }
    
}
