package com.teste.spring.teste;

import com.teste.spring.teste.exception.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTest {

    @Test
    void businessException_aceitaMensagem() {
        BusinessException ex = new BusinessException("msg");
        assertThat(ex.getMessage()).isEqualTo("msg");
    }

    @Test
    void notFoundException_aceitaMensagem() {
        NotFoundException ex = new NotFoundException("not found");
        assertThat(ex.getMessage()).isEqualTo("not found");
    }
}
