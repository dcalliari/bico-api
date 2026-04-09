package com.example.demo.bicos.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bicos.controller.dto.GetBicosByIdDto;
import com.example.demo.bicos.controller.dto.RegisterBicosDto;
import com.example.demo.bicos.controller.dto.UpdateUserByIdDto;
import com.example.demo.bicos.controller.dto.UpdateUserDto;
import com.example.demo.bicos.controller.dto.UpdateUserRoleDto;
import com.example.demo.bicos.models.Bicos;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.models.UserRole;
import com.example.demo.bicos.repo.BicosRepository;
import com.example.demo.bicos.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private BicosRepository bicosRepo;
    
    public List<User> listUsers(){
        return userRepo.findAll();
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

            if (updateUserbyIdDto.mail() != null){
                user.setMail(updateUserbyIdDto.mail());
            }

            userRepo.save(user);
        }
    }

    public Long registerBicos(String userId, RegisterBicosDto registerBicosDto){

        var user = userRepo.findById(UUID.fromString(userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!user.getRole().equals(UserRole.APROVADOR_N1)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas APROVADOR_N1 pode criar bicos.");
    }

    var bicos = new Bicos(
        null,
        user, 
        registerBicosDto.name(),
        registerBicosDto.description(),
        registerBicosDto.city(),
        registerBicosDto.price(),
        null,
        null
    );

    var bicosSaved = bicosRepo.save(bicos);
    return bicosSaved.getId();
}

    public List<GetBicosByIdDto> getBicosById(String userId){
       var user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
       return user.getBicos()
                .stream()
                .map(ad -> new GetBicosByIdDto(ad.getId().toString(), ad.getName(),ad.getDescription(),ad.getCity(),ad.getPrice(), ad.getCreatedAt(), ad.getUpdatedAt()))
                .toList();
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

        if (updateUserDto.mail() != null) {
            user.setMail(updateUserDto.mail());
        }

        userRepo.save(user);
    }
}
    
}
