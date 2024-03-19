package br.com.bankdesafio.apisaldotransferencia.controller;

import static org.mockito.Mockito.doNothing;

import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.service.TransferenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {TransferenciaController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class TransferenciaControllerTest {
    @Autowired
    private TransferenciaController transferenciaController;

    @MockBean
    private TransferenciaService transferenciaService;

    @Test
    @DisplayName("Realiza transferencia entre contas com sucesso")
    void testRealizarTransferencia() throws Exception {
        // Arrange
        doNothing().when(transferenciaService).realizarTransferencia(Mockito.<TransferenciaDTO>any());

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("2fd0f2f6-58f3-4c8b-b510-ec20f6447b44");
        transferenciaDTO.setIdContaOrigem("533caec7-e28b-46be-a796-34fff27c3b63");
        transferenciaDTO.setValor(new BigDecimal("2.3"));
        String content = (new ObjectMapper()).writeValueAsString(transferenciaDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(transferenciaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
