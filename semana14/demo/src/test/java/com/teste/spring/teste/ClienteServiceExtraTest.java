package com.teste.spring.teste;

import com.teste.spring.teste.exception.BusinessException;
import com.teste.spring.teste.exception.NotFoundException;
import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.repository.ClienteRepository;
import com.teste.spring.teste.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceExtraTest {

    @Mock
    ClienteRepository repo;

    @InjectMocks
    ClienteService service;

    // --- TESTE: buscar() deve lançar NotFound ---
    @Test
    void buscar_deveLancarNotFoundQuandoNaoExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    // --- TESTE: excluir() deve buscar e deletar ---
    @Test
    void excluir_deveBuscarEDeletar() {
        Cliente c = new Cliente();
        c.setId(1L);
        c.setNome("Teste");
        c.setEmail("t@ex.com");

        when(repo.findById(1L)).thenReturn(Optional.of(c));

        service.excluir(1L);

        verify(repo).delete(c);
    }

    // --- TESTE: atualizar() deve lançar BusinessException para outro email cadastrado ---
    @Test
    void atualizar_deveLancarBusinessExceptionQuandoEmailPertenceOutroCliente() {
        Cliente existente = new Cliente();
        existente.setId(1L);
        existente.setEmail("old@ex.com");
        existente.setNome("Old");

        Cliente outro = new Cliente();
        outro.setId(2L);
        outro.setEmail("novo@ex.com");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.existsByEmail("novo@ex.com")).thenReturn(true);
        when(repo.findByEmail("novo@ex.com")).thenReturn(Optional.of(outro));

        Cliente dados = new Cliente();
        dados.setNome("Novo");
        dados.setEmail("novo@ex.com");

        assertThatThrownBy(() -> service.atualizar(1L, dados))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email já cadastrado para outro cliente");
    }
}
