package com.example.demo.bicos.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bicos.controller.dto.ListUsersDto;
import com.example.demo.bicos.controller.dto.MyUserDto;
import com.example.demo.bicos.controller.dto.UpdateUserByIdDto;
import com.example.demo.bicos.controller.dto.UpdateUserDto;
import com.example.demo.bicos.controller.dto.UpdateUserRoleDto;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    
    public Page<ListUsersDto> findAllPaginacao(int pagina, int itens) {
    Pageable pageable = PageRequest.of(pagina, itens);
    Page<User> userPage = userRepo.findAll(pageable);
    
    return userPage.map(user -> new ListUsersDto(user));
}


    public Optional<User> getUserById(String userId){
        return userRepo.findById(UUID.fromString(userId));
    }

    public void deleteById(String userId){
        var id = UUID.fromString(userId);

        if (!userRepo.existsById(id)) {
    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    userRepo.deleteById(id);
    }

    public void updateUserById(String userId, UpdateUserByIdDto updateUserbyIdDto){
        var id = UUID.fromString(userId);

        var userEntity = userRepo.findById(id);

        if (userEntity.isPresent()){
            var user = userEntity.get();

            if (updateUserbyIdDto.login() != null){
                user.setLogin(updateUserbyIdDto.login());
            }

            if (updateUserbyIdDto.fullName() != null){
                user.setFullName(updateUserbyIdDto.fullName());
            }

            if (updateUserbyIdDto.mail() != null){
                user.setMail(updateUserbyIdDto.mail());
            }

            userRepo.save(user);
        }
    }

    public void updateUserRole(String userId, UpdateUserRoleDto dto) {
    var id = UUID.fromString(userId);

    var user = userRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    user.setRole(dto.role());
    userRepo.save(user);
}

    public void updateUser(UUID id, UpdateUserDto updateUserDto) {
    var userEntity = userRepo.findById(id);

    if (userEntity.isPresent()) {
        var user = userEntity.get();

        if (updateUserDto.login() != null) {
            user.setLogin(updateUserDto.login());
        }

        if (updateUserDto.fullName() != null) {
            user.setFullName(updateUserDto.fullName());
}

        if (updateUserDto.mail() != null) {
            user.setMail(updateUserDto.mail());
        }

        userRepo.save(user);
    }
}   

    public List<MyUserDto> myUserDetails(String userId) {
    UUID usuarioId = UUID.fromString(userId);
    
    return userRepo.queryById(usuarioId); 
}

}