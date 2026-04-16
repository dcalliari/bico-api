package com.example.demo.bicos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bicos.controller.dto.RejectMotiveDto;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.service.CandidaturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="Candidatura")
@RequestMapping("/api/v1/candidaturas")
public class CandidaturaController {

    @Autowired
    private CandidaturaService candidaturaService;

    @Operation(summary="Canditatar-se em um bico")
    @PreAuthorize("hasAnyRole('FREELANCER', 'ADMIN')")
    @PostMapping("/{id}/candidatar")
    public ResponseEntity<Void> candidatar(@PathVariable Long id){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var candidato = (User) authentication.getPrincipal();
        candidaturaService.candidatar(id, candidato.getId().toString());
        return ResponseEntity.ok().build();
    }

    @Operation(summary="Aprovar uma candidatura")
    @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
    @PostMapping("/{id}/aprovar")
    public ResponseEntity<Void> aprovar(@PathVariable Long id){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var aprovador = (User) authentication.getPrincipal();
        candidaturaService.aprovar(id, aprovador.getId().toString());
        return ResponseEntity.ok().build();
    }

    @Operation(summary="Rejeitar uma candidatura")
    @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
    @PostMapping("/{id}/rejeitar")
    public ResponseEntity<Void> rejeitar(@PathVariable Long id, RejectMotiveDto dto){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var aprovador = (User) authentication.getPrincipal();
        candidaturaService.rejeitar(id, aprovador.getId().toString(), dto.motivo());
        return ResponseEntity.ok().build();
    }

}
