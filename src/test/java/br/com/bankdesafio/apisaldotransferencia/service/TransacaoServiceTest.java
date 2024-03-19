package br.com.bankdesafio.apisaldotransferencia.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import br.com.bankdesafio.apisaldotransferencia.repository.TransacaoRepository;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TransacaoService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class TransacaoServiceTest {
    @MockBean
    private ContaCorrenteRepository contaCorrenteRepository;

    @MockBean
    private TransacaoRepository transacaoRepository;

    @Autowired
    private TransacaoService transacaoService;

    @Test
    @DisplayName("Lança exceção quando as contas não são encontradas")
    void testCriarTransacao() {
        // Arrange
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> transacaoService.criarTransacao(transferenciaDTO, "Nome Cliente Origem", "Nome Cliente Destino"));
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
    }

    @Test
    @DisplayName("Lança exceção quando somente uma conta é encontrada")
    void testCriarTransacao2() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente);
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> transacaoService.criarTransacao(transferenciaDTO, "Nome Cliente Origem", "Nome Cliente Destino"));
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
    }

    @Test
    @DisplayName("Lança exceção quando uma conta ativa e outra inativa são encontradas")
    void testCriarTransacao3() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ContaCorrente contaCorrente2 = new ContaCorrente();
        contaCorrente2.setAtiva(false);
        contaCorrente2.setId("Conta de origem ou destino não encontrada.");
        contaCorrente2.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente2.setSaldo(new BigDecimal("2.3"));
        contaCorrente2.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente2);
        contaCorrenteList.add(contaCorrente);
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> transacaoService.criarTransacao(transferenciaDTO, "Nome Cliente Origem", "Nome Cliente Destino"));
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
    }

    @Test
    @DisplayName("Lança exceção quando a busca por contas falha")
    void testCriarTransacao4() {
        // Arrange
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new IllegalArgumentException("Conta de origem ou destino não encontrada."));

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> transacaoService.criarTransacao(transferenciaDTO, "Nome Cliente Origem", "Nome Cliente Destino"));
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
    }

    @Test
    @DisplayName("Lança exceção quando apenas uma conta válida é passada")
    void testCriarTransacao5() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente);
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("42");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> transacaoService.criarTransacao(transferenciaDTO, "Nome Cliente Origem", "Nome Cliente Destino"));
        verify(contaCorrenteRepository).findContasByIds(eq("42"), eq("Id Conta Destino"));
    }
}
