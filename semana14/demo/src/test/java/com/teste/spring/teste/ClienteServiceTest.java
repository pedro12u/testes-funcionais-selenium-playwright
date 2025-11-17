package com.teste.spring.teste;

import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.exception.*;
import com.teste.spring.teste.repository.ClienteRepository;
import com.teste.spring.teste.service.ClienteService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    ClienteRepository repo;

    @InjectMocks
    ClienteService service;

    @Test
    void criar_deveLancarErroSeEmailExiste() {

        Cliente c = new Cliente();
        c.setEmail("mail@ex.com");

        when(repo.existsByEmail("mail@ex.com")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(c))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email já cadastrado");
    }

    @Test
    void atualizar_deveAlterarCampos() {

        Cliente antigo = new Cliente();
        antigo.setId(1L);
        antigo.setNome("Velho");
        antigo.setEmail("old@ex.com");

        when(repo.findById(1L)).thenReturn(Optional.of(antigo));
        when(repo.existsByEmail("novo@ex.com")).thenReturn(false);
        when(repo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente dados = new Cliente();
        dados.setNome("Novo");
        dados.setEmail("novo@ex.com");

        Cliente atualizado = service.atualizar(1L, dados);

        assertThat(atualizado.getNome()).isEqualTo("Novo");
        assertThat(atualizado.getEmail()).isEqualTo("novo@ex.com");
    }
    
    @Test
    void buscar_deveLancarNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void excluir_deveChamarDelete() {
        Cliente c = new Cliente();
        c.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(c));

        service.excluir(1L);

        verify(repo).delete(c);
    }

    @Test
    void atualizar_deveLancarBusinessExceptionSeEmailDeOutroCliente() {
        Cliente existente = new Cliente();
        existente.setId(1L);

        Cliente outro = new Cliente();
        outro.setId(2L);

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.existsByEmail("x@x.com")).thenReturn(true);
        when(repo.findByEmail("x@x.com")).thenReturn(Optional.of(outro));

        Cliente dados = new Cliente();
        dados.setEmail("x@x.com");

        assertThatThrownBy(() -> service.atualizar(1L, dados))
                .isInstanceOf(BusinessException.class);
    }

}
