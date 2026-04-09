package com.example.demo.bicos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bicos.controller.dto.UpdateUserByIdDto;
import com.example.demo.bicos.controller.dto.UpdateUserRoleDto;
import com.example.demo.bicos.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Admin")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}")
    @Operation(summary="Atualizar usuário por ID")
    public ResponseEntity<Void> updateUserById(
        @PathVariable String userId,
        @RequestBody UpdateUserByIdDto updateUserByIdDto) {

    userService.updateUserById(userId, updateUserByIdDto);
    return ResponseEntity.noContent().build();                            
}

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar usuário por ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }  

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/role")
    @Operation(summary="Atualizar role do usuário por ID")
    public ResponseEntity<Void> updateUserRole(
    @PathVariable String userId,
    @RequestBody UpdateUserRoleDto dto) {

    userService.updateUserRole(userId, dto);
    return ResponseEntity.noContent().build();
}
}
