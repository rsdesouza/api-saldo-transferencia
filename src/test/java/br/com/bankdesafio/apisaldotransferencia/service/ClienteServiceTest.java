package br.com.bankdesafio.apisaldotransferencia.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.bankdesafio.apisaldotransferencia.dto.ClienteResponse;
import br.com.bankdesafio.apisaldotransferencia.rest.ClienteFeignClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ClienteService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ClienteServiceTest {
    @MockBean
    private ClienteFeignClient clienteFeignClient;

    @Autowired
    private ClienteService clienteService;

    @Test
    @DisplayName("Busca o nome do cliente pelo ID na Api de Cadastro")
    void testGetClienteById() {
        // Arrange
        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.setIdConta("a07da740-429f-431e-9474-8a070ed54ceb");
        clienteResponse.setNome("Luke Skywalker");
        when(clienteFeignClient.getClienteById(Mockito.<String>any())).thenReturn(clienteResponse);

        // Act
        ClienteResponse actualClienteById = clienteService.getClienteById("a07da740-429f-431e-9474-8a070ed54ceb");

        // Assert
        verify(clienteFeignClient).getClienteById(eq("a07da740-429f-431e-9474-8a070ed54ceb"));
        assertSame(clienteResponse, actualClienteById);
    }
}
