package com.example.demo.bicos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bicos.controller.dto.BicosPaginadosDto;
import com.example.demo.bicos.controller.dto.CandidaturasDto;
import com.example.demo.bicos.controller.dto.GetBicosByIdDto;
import com.example.demo.bicos.controller.dto.ListBicosDto;
import com.example.demo.bicos.controller.dto.RegisterBicosDto;
import com.example.demo.bicos.controller.dto.UpdateBicosDto;
import com.example.demo.bicos.models.CandidaturaStatus;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.service.BicosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Bicos")
@RestController
@RequestMapping("/api/v1/bicos")
public class BicosController {

    @Autowired
    private BicosService bicosService;

    @Operation(summary = "Listar bicos com filtro por cidade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bicos listados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ListBicosDto>> getAllBicos(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(name= "cidadeId", required = false) Long cidadeId) {
    
        Page<ListBicosDto> bicosPage = bicosService.findAllPaginacao(page, size, cidadeId);
        return ResponseEntity.ok(bicosPage.getContent());
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
    @Operation(summary="Registrar bicos", description = "Filtros: FAXINA, ENTREGAS, MANUTENCAO, TI, EVENTOS, OUTROS. Cidades: 1 - Belém, 2 - Ananindeua, 3 - Marituba")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bico registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de registro inválidos", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content()),
        @ApiResponse(responseCode = "403", description = "Acesso negado para o nível deste usuário", content = @Content())
    })
    public ResponseEntity<Void> registerBicoById(@RequestBody RegisterBicosDto registerBicosDto){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();
        bicosService.registerBicos(user.getId().toString(), registerBicosDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    @Operation(summary="Buscar bicos por ID do usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bicos encontrados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content())
    })
    public ResponseEntity<List<GetBicosByIdDto>> getBicos(@PathVariable("userId") String userId){
        var bicos = bicosService.getBicosById(userId);
        return ResponseEntity.ok(bicos);
    }

    @Operation(summary = "Deletar bicos por ID")
    @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
    @DeleteMapping("/{bicosId}/deletar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bico deletado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content()),
        @ApiResponse(responseCode = "403", description = "Você não tem permissão para deletar este bico", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Bico não encontrado", content = @Content())
    })
    public ResponseEntity<Void> deleteBicos(@PathVariable Long bicosId){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();
        bicosService.deleteBicos(user.getId().toString(), bicosId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar bicos por ID")
    @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
    @PatchMapping("/{bicosId}/atualizar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bico atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de atualização inválidos", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content()),
        @ApiResponse(responseCode = "403", description = "Você não tem permissão para alterar este bico", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Bico não encontrado", content = @Content())
    })
    public ResponseEntity<Void> updateBicos (@PathVariable Long bicosId, @RequestBody UpdateBicosDto updateBicosDto){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();
        
        bicosService.updateBicos(user.getId().toString(), bicosId, updateBicosDto);
        
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ver meus bicos registrados")
    @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bicos listados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content()),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado para este usuário", content = @Content())
    })
    @GetMapping("/usuario")
    public ResponseEntity<List<BicosPaginadosDto>> getMeusBicos(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var aprovador = (User) authentication.getPrincipal();
        
        Page<BicosPaginadosDto> bicosPage = bicosService.meusBicosPaginados(aprovador.getId().toString(), page, size);
        
        return ResponseEntity.ok(bicosPage.getContent());
    }
}