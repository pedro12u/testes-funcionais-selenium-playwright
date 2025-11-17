package com.teste.spring.teste;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.spring.teste.controller.ClienteController;
import com.teste.spring.teste.dto.ClienteDto;
import com.teste.spring.teste.exception.BusinessException;
import com.teste.spring.teste.exception.NotFoundException;
import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.mapa.ClienteMapa;
import com.teste.spring.teste.service.ClienteService;
import com.teste.spring.teste.repository.ClienteRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerExtraTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ClienteService service;

    @MockBean
    ClienteRepository repo;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void criar_deveRetornar201() throws Exception {
        ClienteDto dto = new ClienteDto();
        dto.setNome("Ana");
        dto.setEmail("ana@ex.com");

        Cliente salvo = ClienteMapa.toEntity(dto);
        salvo.setId(1L);

        when(service.criar(any())).thenReturn(salvo);

        mvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void buscar_deveRetornarCliente() throws Exception {
        Cliente c = new Cliente();
        c.setId(1L);
        c.setNome("Ana");
        c.setEmail("ana@ex.com");

        when(service.buscar(1L)).thenReturn(c);

        mvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana"));
    }

    @Test
    void buscar_deveRetornar404() throws Exception {
        when(service.buscar(1L)).thenThrow(new NotFoundException("Cliente não encontrado"));

        mvc.perform(get("/api/clientes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizar_deveFuncionar() throws Exception {
        ClienteDto dto = new ClienteDto();
        dto.setNome("Novo");
        dto.setEmail("novo@ex.com");

        Cliente atualizado = ClienteMapa.toEntity(dto);
        atualizado.setId(1L);

        when(service.atualizar(eq(1L), any())).thenReturn(atualizado);

        mvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo"));
    }

    @Test
    void excluir_deveRetornar204() throws Exception {
        mvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void businessException_deveRetornar422() throws Exception {
        when(service.criar(any())).thenThrow(new BusinessException("Erro"));

        ClienteDto dto = new ClienteDto();
        dto.setNome("Ana");
        dto.setEmail("ana@ex.com");

        mvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity());
    }
}
