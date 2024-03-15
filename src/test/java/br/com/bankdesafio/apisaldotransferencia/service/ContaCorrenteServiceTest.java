package br.com.bankdesafio.apisaldotransferencia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.bankdesafio.apisaldotransferencia.dto.SaldoDTO;
import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.exception.SaldoInsuficienteException;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import br.com.bankdesafio.apisaldotransferencia.service.validation.ContaCorrenteValidationService;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ContaCorrenteService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ContaCorrenteServiceTest {
    @MockBean
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private ContaCorrenteService contaCorrenteService;

    @MockBean
    private ContaCorrenteValidationService contaCorrenteValidationService;

    @Test
    void testConsultarSaldo() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        when(contaCorrenteValidationService.validarContaAtiva(Mockito.<String>any())).thenReturn(contaCorrente);

        // Act
        SaldoDTO actualConsultarSaldoResult = contaCorrenteService.consultarSaldo("42");

        // Assert
        verify(contaCorrenteValidationService).validarContaAtiva(eq("42"));
        assertEquals("42", actualConsultarSaldoResult.getIdConta());
        BigDecimal expectedSaldo = new BigDecimal("2.3");
        assertEquals(expectedSaldo, actualConsultarSaldoResult.getSaldo());
    }

    @Test
    void testConsultarSaldo2() {
        // Arrange
        when(contaCorrenteValidationService.validarContaAtiva(Mockito.<String>any()))
                .thenThrow(new SaldoInsuficienteException("An error occurred"));

        // Act and Assert
        assertThrows(SaldoInsuficienteException.class, () -> contaCorrenteService.consultarSaldo("42"));
        verify(contaCorrenteValidationService).validarContaAtiva(eq("42"));
    }

    @Test
    void testDesativarConta() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        when(contaCorrenteRepository.save(Mockito.<ContaCorrente>any())).thenReturn(contaCorrente);

        ContaCorrente contaCorrente2 = new ContaCorrente();
        contaCorrente2.setAtiva(true);
        contaCorrente2.setId("42");
        contaCorrente2.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente2.setSaldo(new BigDecimal("2.3"));
        contaCorrente2.setTotalTransferidoHoje(new BigDecimal("2.3"));
        when(contaCorrenteValidationService.validarExistenciaConta(Mockito.<String>any())).thenReturn(contaCorrente2);

        // Act
        contaCorrenteService.desativarConta("42");

        // Assert
        verify(contaCorrenteValidationService).validarExistenciaConta(eq("42"));
        verify(contaCorrenteRepository).save(Mockito.<ContaCorrente>any());
    }

    @Test
    void testDesativarConta2() {
        // Arrange
        when(contaCorrenteRepository.save(Mockito.<ContaCorrente>any()))
                .thenThrow(new SaldoInsuficienteException("An error occurred"));

        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        when(contaCorrenteValidationService.validarExistenciaConta(Mockito.<String>any())).thenReturn(contaCorrente);

        // Act and Assert
        assertThrows(SaldoInsuficienteException.class, () -> contaCorrenteService.desativarConta("42"));
        verify(contaCorrenteValidationService).validarExistenciaConta(eq("42"));
        verify(contaCorrenteRepository).save(Mockito.<ContaCorrente>any());
    }

    @Test
    void testAtualizarSaldo() {
        // Arrange
        when(contaCorrenteRepository.atualizarSaldoContaDestino(Mockito.<String>any(), Mockito.<BigDecimal>any()))
                .thenReturn(1);
        when(contaCorrenteRepository.atualizarSaldoContaOrigemComLimiteDiario(Mockito.<String>any(),
                Mockito.<BigDecimal>any())).thenReturn(1);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act
        contaCorrenteService.atualizarSaldo(transferenciaDTO);

        // Assert that nothing has changed
        verify(contaCorrenteRepository).atualizarSaldoContaDestino(eq("Id Conta Destino"), Mockito.<BigDecimal>any());
        verify(contaCorrenteRepository).atualizarSaldoContaOrigemComLimiteDiario(eq("Id Conta Origem"),
                Mockito.<BigDecimal>any());
        assertEquals("Id Conta Destino", transferenciaDTO.getIdContaDestino());
        assertEquals("Id Conta Origem", transferenciaDTO.getIdContaOrigem());
        BigDecimal expectedValor = new BigDecimal("2.3");
        assertEquals(expectedValor, transferenciaDTO.getValor());
    }

    @Test
    void testAtualizarSaldo2() {
        // Arrange
        when(contaCorrenteRepository.atualizarSaldoContaDestino(Mockito.<String>any(), Mockito.<BigDecimal>any()))
                .thenThrow(new SaldoInsuficienteException("An error occurred"));
        when(contaCorrenteRepository.atualizarSaldoContaOrigemComLimiteDiario(Mockito.<String>any(),
                Mockito.<BigDecimal>any())).thenReturn(1);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(SaldoInsuficienteException.class, () -> contaCorrenteService.atualizarSaldo(transferenciaDTO));
        verify(contaCorrenteRepository).atualizarSaldoContaDestino(eq("Id Conta Destino"), Mockito.<BigDecimal>any());
        verify(contaCorrenteRepository).atualizarSaldoContaOrigemComLimiteDiario(eq("Id Conta Origem"),
                Mockito.<BigDecimal>any());
    }

    @Test
    void testAtualizarSaldo3() {
        // Arrange
        when(contaCorrenteRepository.atualizarSaldoContaOrigemComLimiteDiario(Mockito.<String>any(),
                Mockito.<BigDecimal>any())).thenReturn(0);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(SaldoInsuficienteException.class, () -> contaCorrenteService.atualizarSaldo(transferenciaDTO));
        verify(contaCorrenteRepository).atualizarSaldoContaOrigemComLimiteDiario(eq("Id Conta Origem"),
                Mockito.<BigDecimal>any());
    }
}
