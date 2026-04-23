package com.example.demo.bicos.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bicos.controller.dto.GetBicosByIdDto;
import com.example.demo.bicos.controller.dto.RegisterBicosDto;
import com.example.demo.bicos.models.Bicos;
import com.example.demo.bicos.models.UserRole;
import com.example.demo.bicos.repo.BicosRepository;
import com.example.demo.bicos.repo.UserRepository;

@Service
public class BicosService {

    @Autowired
    private BicosRepository bicosRepo;

    @Autowired
    private UserRepository userRepo;

    public List<Bicos> listBicos(){
        return bicosRepo.findAll();
    }

    public Long registerBicos(String userId, RegisterBicosDto registerBicosDto){

        var user = userRepo.findById(UUID.fromString(userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user.getRole().equals(UserRole.FREELANCER)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas FREELANCER não é permitido criar bicos.");
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
}
