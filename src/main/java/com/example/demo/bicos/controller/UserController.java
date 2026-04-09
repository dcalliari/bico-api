package com.example.demo.bicos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bicos.controller.dto.ListUsersDto;
import com.example.demo.bicos.controller.dto.UpdateUserDto;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="Usuários")
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;

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
        public ResponseEntity<ListUsersDto> getUserById(@PathVariable("userId") String userId) {
        return userService.getUserById(userId)
           .map(user -> ResponseEntity.ok(new ListUsersDto(user)))
               .orElseGet(() -> ResponseEntity.notFound().build());
        }
    
    @Operation(summary="Atualizar dados do usuário")
    @PatchMapping("/me")
    public ResponseEntity<Void> updateMe(@RequestBody UpdateUserDto updateUserDto) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = (User) authentication.getPrincipal();
    
    userService.updateUser(user.getId(), updateUserDto);
    
    return ResponseEntity.ok().build();
}
}
