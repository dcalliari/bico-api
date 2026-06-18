package com.example.demo.bicos.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bicos.controller.dto.GetBicosByIdDto;
import com.example.demo.bicos.controller.dto.ListBicosDto;
import com.example.demo.bicos.controller.dto.RegisterBicosDto;
import com.example.demo.bicos.controller.dto.UpdateBicosDto;
import com.example.demo.bicos.models.Bicos;
import com.example.demo.bicos.models.Cidade; // Importação necessária
import com.example.demo.bicos.models.UserRole;
import com.example.demo.bicos.repo.BicosRepository;
import com.example.demo.bicos.repo.CidadeRepository; // Injetado abaixo
import com.example.demo.bicos.repo.UserRepository;

@Service
public class BicosService {

    @Autowired
    private BicosRepository bicosRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CidadeRepository cidadeRepo;

    public Page<ListBicosDto> findAllPaginacao(int pagina, int itens, Long cidadeId) {
        Pageable pageable = PageRequest.of(pagina, itens);
        Page<Bicos> bicosPage;

        if (cidadeId != null) {
            bicosPage = bicosRepo.findByCidadeId(cidadeId, pageable);
        } else {
            bicosPage = bicosRepo.findAll(pageable);
        }
        
        return bicosPage.map(ListBicosDto::new);
    }

    public Long registerBicos(String userId, RegisterBicosDto registerBicosDto) {

        var user = userRepo.findById(UUID.fromString(userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        if (user.getRole().equals(UserRole.FREELANCER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas contratantes podem criar bicos.");
        }

        Cidade cidade = cidadeRepo.getReferenceById(registerBicosDto.cidadeId());

        var bicos = new Bicos(
            null,
            user, 
            registerBicosDto.name(),
            registerBicosDto.description(),
            cidade,
            registerBicosDto.price(),
            registerBicosDto.bicosFilter(),
            registerBicosDto.dataHoraServico(),
            null,
            null,
            null
        );

        var bicosSaved = bicosRepo.save(bicos);
        return bicosSaved.getId();
    }

    public List<GetBicosByIdDto> getBicosById(String userId) {
        var user = userRepo.findById(UUID.fromString(userId))
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
         
        return user.getBicos()
                 .stream()
                 .map(ad -> new GetBicosByIdDto(
                     ad.getId().toString(), 
                     ad.getName(),
                     ad.getDescription(),
                     ad.getCidade().getName(),
                     ad.getPrice(),
                     ad.getBicosFilter(), 
                     ad.getDataHoraServico(), 
                     ad.getCreatedAt(), 
                     ad.getUpdatedAt(), 
                     ad.getDeletedAt()
                 ))
                 .toList();
    }

    public void deleteBicos(String userId, Long bicoId) {
        var user = userRepo.findById(UUID.fromString(userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        var bicos = bicosRepo.findById(bicoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bico não encontrado."));

        if (!bicos.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para deletar este bico");
        }
        bicosRepo.delete(bicos);
    }

    public void updateBicos(String userId, Long bicoId, UpdateBicosDto dto) {
        var user = userRepo.findById(UUID.fromString(userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        var bicos = bicosRepo.findById(bicoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bico não encontrado."));

        if (!bicos.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para atualizar este bico");
        }   

        if (dto.name() != null) {
            bicos.setName(dto.name());
        }

        if (dto.description() != null) {
            bicos.setDescription(dto.description());
        }
        
        if (dto.cidadeId() != null) {
            Cidade novaCidade = cidadeRepo.getReferenceById(dto.cidadeId());
            bicos.setCidade(novaCidade);
        }

        if (dto.price() != null) {
            bicos.setPrice(dto.price());
        }

        if (dto.bicosFilter() != null) {
            bicos.setBicosFilter(dto.bicosFilter());
        }

        if (dto.dataHoraServico() != null) {
            bicos.setDataHoraServico(dto.dataHoraServico());
        }

        bicosRepo.save(bicos);
    }
}