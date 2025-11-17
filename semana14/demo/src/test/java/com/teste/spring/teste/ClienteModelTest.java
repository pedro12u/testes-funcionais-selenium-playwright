package com.teste.spring.teste;

import com.teste.spring.teste.model.Cliente;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ClienteModelTest {

    @Test
    void deveSetarEObterCamposCorretamente() {
        Cliente c = new Cliente();

        c.setId(1L);
        c.setNome("Ana");
        c.setEmail("ana@ex.com");
        c.setTelefone("123");

        assertThat(c.getId()).isEqualTo(1L);
        assertThat(c.getNome()).isEqualTo("Ana");
        assertThat(c.getEmail()).isEqualTo("ana@ex.com");
        assertThat(c.getTelefone()).isEqualTo("123");
    }
}
