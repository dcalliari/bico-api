package com.example.demo.bicos.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bicos.controller.dto.ListUsersDto;
import com.example.demo.bicos.controller.dto.UpdateUserDto;
import com.example.demo.bicos.controller.dto.UpdateUserRoleDto;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.repo.UserRepository;
import com.example.demo.bicos.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="Usuários")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepo;

    public UserController(UserService userService, UserRepository userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @Operation(summary = "Listar usuários")
    @GetMapping
    public ResponseEntity<List<ListUsersDto>> getAllUsers(){
        List<ListUsersDto> users = userService.listUsers()
        .stream()
        .map(ListUsersDto::new)
        .collect(Collectors.toList());

    return ResponseEntity.ok(users);
    }

    @Operation(summary = "Buscar usuário por ID")
    @GetMapping("/{userId}")
        public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        var user = userService.getUserById(userId);
       
        return user.map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
        }
    
    @PatchMapping("/{userId}")
    @Operation(summary="Atualizar usuário por ID")
    public void updateUserById(String userId, UpdateUserDto updateUserDto){
    var id = UUID.fromString(userId);
    var user = userRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    
    if (updateUserDto.login() != null) {
        user.setLogin(updateUserDto.login());
    }
    if (updateUserDto.mail() != null) {
        user.setMail(updateUserDto.mail());
    }
    userRepo.save(user);
}

    @Operation(summary = "Deletar usuário por ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }  

    @PreAuthorize("hasRole('APROVADOR_N3')")
    @PatchMapping("/{userId}/role")
    @Operation(summary="Atualizar role do usuário (APROVADOR_N3)")
    public ResponseEntity<Void> updateUserRole(
    @PathVariable String userId,
    @RequestBody UpdateUserRoleDto dto) {

    userService.updateUserRole(userId, dto);
    return ResponseEntity.noContent().build();
}
    
}
