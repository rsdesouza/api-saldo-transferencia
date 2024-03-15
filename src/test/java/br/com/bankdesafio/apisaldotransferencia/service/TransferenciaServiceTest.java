package br.com.bankdesafio.apisaldotransferencia.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.bankdesafio.apisaldotransferencia.dto.ClienteResponse;
import br.com.bankdesafio.apisaldotransferencia.dto.NotificacaoBacenDTO;
import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.model.Transacao;
import br.com.bankdesafio.apisaldotransferencia.service.validation.TransferenciaValidationService;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TransferenciaService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class TransferenciaServiceTest {
    @MockBean
    private BACENNotificationService bACENNotificationService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ContaCorrenteService contaCorrenteService;

    @MockBean
    private TransacaoService transacaoService;

    @Autowired
    private TransferenciaService transferenciaService;

    @MockBean
    private TransferenciaValidationService transferenciaValidationService;

    @Test
    void testRealizarTransferencia() {
        // Arrange
        doNothing().when(contaCorrenteService).atualizarSaldo(Mockito.<TransferenciaDTO>any());

        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.setIdConta("Id Conta");
        clienteResponse.setNome("Nome");
        when(clienteService.getClienteById(Mockito.<String>any())).thenReturn(clienteResponse);

        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setAtiva(true);
        contaDestino.setId("42");
        contaDestino.setLimiteDiario(new BigDecimal("2.3"));
        contaDestino.setSaldo(new BigDecimal("2.3"));
        contaDestino.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ContaCorrente contaOrigem = new ContaCorrente();
        contaOrigem.setAtiva(true);
        contaOrigem.setId("42");
        contaOrigem.setLimiteDiario(new BigDecimal("2.3"));
        contaOrigem.setSaldo(new BigDecimal("2.3"));
        contaOrigem.setTotalTransferidoHoje(new BigDecimal("2.3"));

        Transacao transacao = new Transacao();
        transacao.setContaDestino(contaDestino);
        transacao.setContaOrigem(contaOrigem);
        transacao.setDataHoraTransacao(LocalDate.of(1970, 1, 1).atStartOfDay());
        transacao.setId("42");
        transacao.setNomeClienteDestino("Nome Cliente Destino");
        transacao.setNomeClienteOrigem("Nome Cliente Origem");
        transacao.setValor(new BigDecimal("2.3"));
        when(transacaoService.criarTransacao(Mockito.<TransferenciaDTO>any(), Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(transacao);
        doNothing().when(bACENNotificationService).notificarBACEN(Mockito.<NotificacaoBacenDTO>any());
        doNothing().when(transferenciaValidationService).validarTransferencia(Mockito.<TransferenciaDTO>any());

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act
        transferenciaService.realizarTransferencia(transferenciaDTO);

        // Assert
        verify(bACENNotificationService).notificarBACEN(Mockito.<NotificacaoBacenDTO>any());
        verify(clienteService, atLeast(1)).getClienteById(Mockito.<String>any());
        verify(contaCorrenteService).atualizarSaldo(Mockito.<TransferenciaDTO>any());
        verify(transacaoService).criarTransacao(Mockito.<TransferenciaDTO>any(), eq("Nome"), eq("Nome"));
        verify(transferenciaValidationService).validarTransferencia(Mockito.<TransferenciaDTO>any());
    }
}
