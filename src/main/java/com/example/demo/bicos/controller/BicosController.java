package com.example.demo.bicos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bicos.controller.dto.GetBicosByIdDto;
import com.example.demo.bicos.controller.dto.ListBicosDto;
import com.example.demo.bicos.controller.dto.RegisterBicosDto;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.service.BicosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Bicos")
@RestController
@RequestMapping("/api/v1/bicos")
public class BicosController {

    @Autowired
    private BicosService bicosService;

    @Operation(summary = "Listar bicos")
    @GetMapping
    public ResponseEntity<List<ListBicosDto>> getAllBicos(){
        List<ListBicosDto> users = bicosService.listBicos()
        .stream()
        .map(ListBicosDto::new)
        .collect(Collectors.toList());

    return ResponseEntity.ok(users);
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasAnyRole('APROVADOR_N1', 'APROVADOR_N2', 'APROVADOR_N3', 'ADMIN')")
    @Operation(summary="Registrar bicos")
    public ResponseEntity<Void> registerBicoById(@RequestBody RegisterBicosDto registerBicosDto){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();
        bicosService.registerBicos(user.getId().toString(), registerBicosDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    @Operation(summary="Buscar bicos por ID do usuário")
    public ResponseEntity<List<GetBicosByIdDto>> getBicos(@PathVariable("userId") String userId){
        var bicos = bicosService.getBicosById(userId);
        return ResponseEntity.ok(bicos);
    }
}
