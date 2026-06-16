package com.example.demo.bicos.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@SQLDelete(sql = "UPDATE bicos SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at is NULL")
@Table(name="bicos")
public class Bicos {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable=false)
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "cidade_id")
    private Cidade cidade;

    @Column(nullable=false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private BicosFilter bicosFilter;

    @Column(nullable=false)
    private LocalDateTime dataHoraServico;

    @CreationTimestamp
    @Column(name="created_at", updatable=false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;
    
    public Bicos() {
    }

    public Bicos(Long id, User user, String name, String description, Cidade cidade, BigDecimal price,
            BicosFilter bicosFilter, LocalDateTime dataHoraServico, Instant createdAt, Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.description = description;
        this.cidade = cidade;
        this.price = price;
        this.bicosFilter = bicosFilter;
        this.dataHoraServico = dataHoraServico;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Cidade getCidade() {
        return cidade;
    }

    // CORRIGIDO COMPLETAMENTE
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BicosFilter getBicosFilter() {
        return bicosFilter;
    }

    public void setBicosFilter(BicosFilter bicosFilter) {
        this.bicosFilter = bicosFilter;
    }

    public LocalDateTime getDataHoraServico() {
        return dataHoraServico;
    }

    public void setDataHoraServico(LocalDateTime dataHoraServico) {
        this.dataHoraServico = dataHoraServico;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}