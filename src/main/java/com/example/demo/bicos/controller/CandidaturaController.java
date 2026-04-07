package com.example.demo.bicos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bicos.models.User;
import com.example.demo.bicos.service.CandidaturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="Candidatura")
@RequestMapping("/api/candidaturas")
public class CandidaturaController {

    private final CandidaturaService candidaturaService;

    public CandidaturaController(CandidaturaService candidaturaService) {
        this.candidaturaService = candidaturaService;
    }

    @Operation(summary="Canditatar-se em um bico")
    @PostMapping("/{id}/candidatar")
    public ResponseEntity<Void> candidatar(@PathVariable Long id){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var candidato = (User) authentication.getPrincipal();
        candidaturaService.candidatar(id, candidato.getId().toString());
        return ResponseEntity.ok().build();
    }

    @Operation(summary="Aprovar uma candidatura")
    @PostMapping("/{id}/aprovar")
    public ResponseEntity<Void> aprovar(@PathVariable Long id){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var aprovador = (User) authentication.getPrincipal();
        candidaturaService.aprovar(id, aprovador.getId().toString());
        return ResponseEntity.ok().build();
    }

    @Operation(summary="Rejeitar uma candidatura")
    @PostMapping("/{id}/rejeitar")
    public ResponseEntity<Void> rejeitar(@PathVariable Long id){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var aprovador = (User) authentication.getPrincipal();
        candidaturaService.rejeitar(id, aprovador.getId().toString());
        return ResponseEntity.ok().build();
    }

}
