package com.teste.spring.teste;

import com.teste.spring.teste.dto.ClienteDto;
import com.teste.spring.teste.mapa.ClienteMapa;
import com.teste.spring.teste.model.Cliente;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteMapaTest {

    @Test
    void toEntity_deveConverterDtoParaEntity() {
        ClienteDto dto = new ClienteDto();
        dto.setId(5L);
        dto.setNome("João");
        dto.setEmail("j@ex.com");
        dto.setTelefone("999");

        Cliente e = ClienteMapa.toEntity(dto);

        assertThat(e.getId()).isEqualTo(5L);
        assertThat(e.getNome()).isEqualTo("João");
        assertThat(e.getEmail()).isEqualTo("j@ex.com");
        assertThat(e.getTelefone()).isEqualTo("999");
    }

    @Test
    void toDTO_deveConverterEntityParaDto() {
        Cliente c = new Cliente();
        c.setId(10L);
        c.setNome("Maria");
        c.setEmail("m@ex.com");
        c.setTelefone("888");

        ClienteDto dto = ClienteMapa.toDTO(c);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getNome()).isEqualTo("Maria");
        assertThat(dto.getEmail()).isEqualTo("m@ex.com");
        assertThat(dto.getTelefone()).isEqualTo("888");
    }
}
