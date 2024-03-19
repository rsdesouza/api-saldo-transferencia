package br.com.bankdesafio.apisaldotransferencia.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import br.com.bankdesafio.apisaldotransferencia.dto.SaldoDTO;
import br.com.bankdesafio.apisaldotransferencia.service.ContaCorrenteService;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.junit.jupiter.api.DisplayName;

@ContextConfiguration(classes = {ContaCorrenteController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ContaCorrenteControllerTest {

    @Autowired
    private ContaCorrenteController contaCorrenteController;

    @MockBean
    private ContaCorrenteService contaCorrenteService;

    @Test
    @DisplayName("Consultar saldo retorna saldo correto")
    void testConsultarSaldo() throws Exception {
        // Arrange
        SaldoDTO saldoDTO = new SaldoDTO();
        saldoDTO.setIdConta("a07da740-429f-431e-9474-8a070ed54ceb");
        saldoDTO.setSaldo(new BigDecimal("42.3"));
        when(contaCorrenteService.consultarSaldo(Mockito.<String>any())).thenReturn(saldoDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/contas/{id}/saldo", "42");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(contaCorrenteController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id_conta\":\"a07da740-429f-431e-9474-8a070ed54ceb\",\"saldo\":42.3}"));
    }

    @Test
    @DisplayName("Desativar conta com sucesso")
    void testDesativarConta() throws Exception {
        // Arrange
        doNothing().when(contaCorrenteService).desativarConta(Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/contas/{id}", "42");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(contaCorrenteController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Desativar conta com tipo de conteúdo específico")
    void testDesativarConta2() throws Exception {
        // Arrange
        doNothing().when(contaCorrenteService).desativarConta(Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/contas/{id}", "42");
        requestBuilder.contentType("https://example.org/example");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(contaCorrenteController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
