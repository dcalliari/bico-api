package com.example.demo.bicos.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bicos.controller.dto.CandidaturaPendenteDto;
import com.example.demo.bicos.controller.dto.CandidaturasDto;
import com.example.demo.bicos.controller.dto.RejectMotiveDto;
import com.example.demo.bicos.models.CandidaturaStatus;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.service.CandidaturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Candidatura")
@RequestMapping("/api/v1/candidaturas")
public class CandidaturaController {

        private final CandidaturaService candidaturaService;

        public CandidaturaController(CandidaturaService candidaturaService) {
                this.candidaturaService = candidaturaService;
        }

        @Operation(summary = "Canditatar-se em um bico")
        @PreAuthorize("hasAnyRole('FREELANCER', 'ADMIN')")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Candidatura realizada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Já se candidatou para este bico"),
                        @ApiResponse(responseCode = "404", description = "Bico não encontrado")
        })
        @PostMapping("/{id}/candidatar")
        public ResponseEntity<Void> candidatar(@PathVariable Long id) {
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                var candidato = (User) authentication.getPrincipal();
                candidaturaService.candidatar(id, candidato.getId().toString());
                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Aprovar uma candidatura")
        @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Candidatura aprovada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Candidatura já aprovada ou rejeitada"),
                        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                        @ApiResponse(responseCode = "403", description = "Aprovação não autorizada para este usuário"),
                        @ApiResponse(responseCode = "404", description = "Candidatura não encontrada")
        })
        @PostMapping("/{id}/aprovar")
        public ResponseEntity<Void> aprovar(@PathVariable Long id) {
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                var aprovador = (User) authentication.getPrincipal();
                candidaturaService.aprovar(id, aprovador.getId().toString());
                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Rejeitar uma candidatura")
        @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Candidatura rejeitada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Candidatura já aprovada ou rejeitada"),
                        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                        @ApiResponse(responseCode = "403", description = "Rejeição não autorizada para este usuário"),
                        @ApiResponse(responseCode = "404", description = "Candidatura não encontrada")
        })
        @PostMapping("/{id}/rejeitar")
        public ResponseEntity<Void> rejeitar(@PathVariable Long id, RejectMotiveDto dto) {
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                var aprovador = (User) authentication.getPrincipal();
                candidaturaService.rejeitar(id, aprovador.getId().toString(), dto.motivo());
                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Ver minhas candidaturas com filtro por status")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Candidaturas listadas com sucesso"),
                        @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content()),
                        @ApiResponse(responseCode = "403", description = "Acesso não autorizado para este usuário", content = @Content())
        })
        @PreAuthorize("hasAnyRole('FREELANCER', 'ADMIN')")
        @GetMapping("/usuario")
        public ResponseEntity<List<CandidaturasDto>> listarMinhasCandidaturas(
                        @RequestParam(required = false) CandidaturaStatus status,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                var authentication = SecurityContextHolder.getContext().getAuthentication();
                var candidato = (User) authentication.getPrincipal();

                Page<CandidaturasDto> bicosPage = candidaturaService.minhasCandidaturasPaginadas(
                                candidato.getId().toString(),
                                page, size);

                return ResponseEntity.ok(bicosPage.getContent());
        }

        @Operation(summary = "Ver candidaturas pendentes no meu nível de aprovação")
        @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Candidaturas pendentes listadas com sucesso"),
                        @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content()),
                        @ApiResponse(responseCode = "403", description = "Acesso não autorizado para este usuário", content = @Content())
        })
        @GetMapping("/pendentes-nivel")
        public ResponseEntity<List<CandidaturaPendenteDto>> nivelPendente(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                var authentication = SecurityContextHolder.getContext().getAuthentication();
                var aprovador = (User) authentication.getPrincipal();

                Page<CandidaturaPendenteDto> bicosPage = candidaturaService.nivelPendentePaginado(
                                aprovador.getId().toString(),
                                page, size);

                return ResponseEntity.ok(bicosPage.getContent());
        }
}