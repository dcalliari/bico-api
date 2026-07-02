package com.example.demo.bicos.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.bicos.models.Bicos;
import com.example.demo.bicos.models.BicosFilter;
import com.example.demo.bicos.models.Candidatura;
import com.example.demo.bicos.models.CandidaturaStatus;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.models.UserRole;
import com.example.demo.bicos.repo.BicosRepository;
import com.example.demo.bicos.repo.CandidaturaRepository;
import com.example.demo.bicos.repo.CidadeRepository;
import com.example.demo.bicos.repo.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class CandidaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CandidaturaRepository candidaturaRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BicosRepository bicosRepo;
    @Autowired
    private CidadeRepository cidadeRepo;

    private final String BASE_URL = "/api/v1/candidaturas";

    private User criarUsuario(UserRole role) {
        User user = new User();
        user.setFullName("User " + role);
        user.setMail(UUID.randomUUID() + "@teste.com");
        user.setCpf(UUID.randomUUID().toString().substring(0, 11));
        user.setRole(role);
        user.setPassword("123");
        user.setLogin("user_" + role + "_" + UUID.randomUUID());
        return userRepo.save(user);
    }

    private Bicos criarBico() {
        User dono = criarUsuario(UserRole.ADMIN);
        Bicos bico = new Bicos();
        bico.setName("Bico Teste");
        bico.setPrice(new BigDecimal("100.00"));
        var cidade = cidadeRepo.findById(1L).orElse(null);
        bico.setCidade(cidade);
        bico.setDataHoraServico(LocalDateTime.now().plusSeconds(3600));
        bico.setUser(dono);
        bico.setBicosFilter(BicosFilter.TI);

        return bicosRepo.save(bico);
    }

    private Bicos criarBico2() {
        User dono = criarUsuario(UserRole.ADMIN);
        Bicos bico = new Bicos();
        bico.setName("Bico Teste 2");
        bico.setPrice(new BigDecimal("200.00"));
        var cidade = cidadeRepo.findById(1L).orElse(null);
        bico.setCidade(cidade);
        bico.setDataHoraServico(LocalDateTime.now().plusSeconds(3600));
        bico.setUser(dono);
        bico.setBicosFilter(BicosFilter.TI);

        return bicosRepo.save(bico);
    }

    private Candidatura criarCandidatura(Bicos bico, CandidaturaStatus status) {
        Candidatura cand = new Candidatura();
        cand.setBicos(bico);
        cand.setStatus(status);
        cand.setUser(criarUsuario(UserRole.FREELANCER));
        return candidaturaRepo.save(cand);
    }

    @Test
    @DisplayName("Deve passar de PENDENTE para AGUARDANDO_N2 quando N1 aprova")
    void testFluxoAprovacaoN1() throws Exception {
        Bicos bico = criarBico();
        Candidatura cand = criarCandidatura(bico, CandidaturaStatus.PENDENTE);

        User n1 = criarUsuario(UserRole.APROVADOR_N1);

        mockMvc.perform(post(BASE_URL + "/" + cand.getId() + "/aprovar").with(user(n1)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve passar de AGUARDANDO_N2 para AGUARDANDO_N3 quando N2 aprova")
    void testFluxoAprovacaoN2() throws Exception {
        Bicos bico = criarBico();
        Candidatura cand = criarCandidatura(bico, CandidaturaStatus.AGUARDANDO_N2);
        User n2 = criarUsuario(UserRole.APROVADOR_N2);

        mockMvc.perform(post(BASE_URL + "/" + cand.getId() + "/aprovar").with(user(n2)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve passar para APROVADO quando N3 aprova")
    void testFluxoAprovacaoN3() throws Exception {
        Bicos bico = criarBico();
        Candidatura cand = criarCandidatura(bico, CandidaturaStatus.AGUARDANDO_N3);
        User n3 = criarUsuario(UserRole.APROVADOR_N3);

        mockMvc.perform(post(BASE_URL + "/" + cand.getId() + "/aprovar").with(user(n3)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve dar 400 se N2 tentar aprovar algo que ainda está PENDENTE (pulando etapa)")
    void testErroFluxoN2() throws Exception {
        Bicos bico = criarBico();
        Candidatura cand = criarCandidatura(bico, CandidaturaStatus.PENDENTE);
        User n2 = criarUsuario(UserRole.APROVADOR_N2);

        mockMvc.perform(post(BASE_URL + "/" + cand.getId() + "/aprovar").with(user(n2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve dar 400 se tentar se candidatar duas vezes no mesmo bico")
    void testCandidaturaDuplicada() throws Exception {
        Bicos bico = criarBico();
        User freelancer = criarUsuario(UserRole.FREELANCER);

        mockMvc.perform(post(BASE_URL + "/" + bico.getId() + "/candidatar").with(user(freelancer)))
                .andExpect(status().isOk());

        mockMvc.perform(post(BASE_URL + "/" + bico.getId() + "/candidatar").with(user(freelancer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("N2 deve conseguir rejeitar candidatura em seu nível")
    void testRejeicaoN2() throws Exception {
        Bicos bico = criarBico();
        Candidatura cand = criarCandidatura(bico, CandidaturaStatus.AGUARDANDO_N2);
        User n2 = criarUsuario(UserRole.APROVADOR_N2);

        mockMvc.perform(post(BASE_URL + "/" + cand.getId() + "/rejeitar")
                .with(user(n2))
                .param("motivo", "Documentação incompleta"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Listar minhas candidaturas com filtro por status")
    void testListarMinhasCandidaturas() throws Exception {
        User freelancer = criarUsuario(UserRole.FREELANCER);

        Bicos bico1 = criarBico();
        Bicos bico2 = criarBico2();

        Candidatura c1 = new Candidatura();
        c1.setBicos(bico1);
        c1.setStatus(CandidaturaStatus.PENDENTE);
        c1.setUser(freelancer);
        candidaturaRepo.save(c1);

        Candidatura c2 = new Candidatura();
        c2.setBicos(bico2);
        c2.setStatus(CandidaturaStatus.AGUARDANDO_N2);
        c2.setUser(freelancer);
        candidaturaRepo.save(c2);

        mockMvc.perform(get(BASE_URL + "/usuario")
                .with(user(freelancer))
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Listar candidaturas pendentes no nivel N1")
    void testListarCandidaturasPendentesN1() throws Exception {
        User n1 = criarUsuario(UserRole.APROVADOR_N1);

        Bicos bico1 = criarBico();
        Bicos bico2 = criarBico2();

        Candidatura c1 = new Candidatura();
        c1.setBicos(bico1);
        c1.setStatus(CandidaturaStatus.PENDENTE);
        c1.setUser(criarUsuario(UserRole.FREELANCER));
        candidaturaRepo.save(c1);

        Candidatura c2 = new Candidatura();
        c2.setBicos(bico2);
        c2.setStatus(CandidaturaStatus.PENDENTE);
        c2.setUser(criarUsuario(UserRole.FREELANCER));
        candidaturaRepo.save(c2);

        mockMvc.perform(get(BASE_URL + "/pendentes-nivel")
                .with(user(n1))
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

}