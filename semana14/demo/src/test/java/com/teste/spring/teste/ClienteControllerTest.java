package com.teste.spring.teste;

import com.teste.spring.teste.controller.ClienteController;
import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.repository.ClienteRepository;
import com.teste.spring.teste.service.ClienteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ClienteRepository repo;

    @MockBean
    ClienteService service;

    @Test
    void listar_deveRetornarPaginaDeClientes() throws Exception {

        Cliente c1 = new Cliente();
        c1.setId(1L);
        c1.setNome("Ana");
        c1.setEmail("ana@ex.com");

        Cliente c2 = new Cliente();
        c2.setId(2L);
        c2.setNome("Bruno");
        c2.setEmail("bruno@ex.com");

        Page<Cliente> pagina = new PageImpl<>(
                List.of(c1, c2),
                PageRequest.of(0,10),
                2
        );

        when(repo.findAll(any(Pageable.class))).thenReturn(pagina);

        mvc.perform(get("/api/clientes")
                .param("page","0")
                .param("size","10")
                .param("sort","id,asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].nome").value("Ana"))
                .andExpect(jsonPath("$.content[1].email").value("bruno@ex.com"));
    }
}
