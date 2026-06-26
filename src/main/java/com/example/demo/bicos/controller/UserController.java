package com.example.demo.bicos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bicos.controller.dto.ListUsersDto;
import com.example.demo.bicos.controller.dto.MyUserDto;
import com.example.demo.bicos.controller.dto.UpdateUserDto;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="Usuários")
@RequestMapping("/api/v1/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Operation(summary = "Listar usuários")
    @GetMapping
    public ResponseEntity<List<ListUsersDto>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    Page<ListUsersDto> userPage = userService.findAllPaginacao(page, size);
    
    return ResponseEntity.ok(userPage.getContent());
}

    @Operation(summary = "Buscar usuário por ID")
    @GetMapping("/{userId}")
        public ResponseEntity<ListUsersDto> getUserById(@PathVariable("userId") String userId) {
        return userService.getUserById(userId)
           .map(user -> ResponseEntity.ok(new ListUsersDto(user)))
               .orElseGet(() -> ResponseEntity.notFound().build());
        }
    
    @Operation(summary="Atualizar dados do usuário")
    @PatchMapping("/me/update")
    public ResponseEntity<Void> updateMe(@RequestBody UpdateUserDto updateUserDto) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = (User) authentication.getPrincipal();
    
    userService.updateUser(user.getId(), updateUserDto);
    
    return ResponseEntity.ok().build();
    }

    @Operation(summary = "Detalhes do usuário autenticado")
    @GetMapping("/me")
    public ResponseEntity<List<MyUserDto>> myUserDetails() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = (User) authentication.getPrincipal();

    return ResponseEntity.ok(userService.myUserDetails(user.getId().toString()));
    }
}
