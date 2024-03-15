package br.com.bankdesafio.apisaldotransferencia.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.exception.ContaInativaException;
import br.com.bankdesafio.apisaldotransferencia.exception.ContaNotFoundException;
import br.com.bankdesafio.apisaldotransferencia.exception.LimiteDiarioExcedidoException;
import br.com.bankdesafio.apisaldotransferencia.exception.SaldoInsuficienteException;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TransferenciaValidationService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class TransferenciaValidationServiceTest {
    @MockBean
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private TransferenciaValidationService transferenciaValidationService;

    @Test
    void testValidarTransferencia() {
        // Arrange
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(ContaNotFoundException.class,
                () -> transferenciaValidationService.validarTransferencia(transferenciaDTO));
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
    }

    @Test
    void testValidarTransferencia2() {
        // Arrange
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new ContaNotFoundException("An error occurred"));

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(ContaNotFoundException.class,
                () -> transferenciaValidationService.validarTransferencia(transferenciaDTO));
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
    }

    @Test
    void testValidarTransferencia3() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ContaCorrente contaCorrente2 = new ContaCorrente();
        contaCorrente2.setAtiva(false);
        contaCorrente2.setId("Id");
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
        assertThrows(ContaInativaException.class,
                () -> transferenciaValidationService.validarTransferencia(transferenciaDTO));
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
    }

    @Test
    void testValidarTransferencia4() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ContaCorrente contaCorrente2 = new ContaCorrente();
        contaCorrente2.setAtiva(false);
        contaCorrente2.setId("Id");
        contaCorrente2.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente2.setSaldo(new BigDecimal("2.3"));
        contaCorrente2.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ContaCorrente contaCorrente3 = new ContaCorrente();
        contaCorrente3.setAtiva(true);
        contaCorrente3.setId("1000");
        contaCorrente3.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente3.setSaldo(new BigDecimal("2.3"));
        contaCorrente3.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente3);
        contaCorrenteList.add(contaCorrente2);
        contaCorrenteList.add(contaCorrente);
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(ContaInativaException.class,
                () -> transferenciaValidationService.validarTransferencia(transferenciaDTO));
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
    }

    @Test
    void testValidarTransferencia5() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        ContaCorrente contaCorrente2 = mock(ContaCorrente.class);
        when(contaCorrente2.getAtiva()).thenReturn(true);
        doNothing().when(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente2).setId(Mockito.<String>any());
        doNothing().when(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente2.setAtiva(false);
        contaCorrente2.setId("Id");
        contaCorrente2.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente2.setSaldo(new BigDecimal("2.3"));
        contaCorrente2.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente2);
        contaCorrenteList.add(contaCorrente);

        ContaCorrente contaCorrente3 = new ContaCorrente();
        contaCorrente3.setAtiva(true);
        contaCorrente3.setId("42");
        contaCorrente3.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente3.setSaldo(new BigDecimal("2.3"));
        contaCorrente3.setTotalTransferidoHoje(new BigDecimal("2.3"));
        Optional<ContaCorrente> ofResult = Optional.of(contaCorrente3);
        when(contaCorrenteRepository.findById(Mockito.<String>any())).thenReturn(ofResult);
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(LimiteDiarioExcedidoException.class,
                () -> transferenciaValidationService.validarTransferencia(transferenciaDTO));
        verify(contaCorrente2).getAtiva();
        verify(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente2).setId(eq("Id"));
        verify(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
        verify(contaCorrenteRepository, atLeast(1)).findById(eq("Id Conta Origem"));
    }

    @Test
    void testValidarTransferencia6() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        ContaCorrente contaCorrente2 = mock(ContaCorrente.class);
        when(contaCorrente2.getAtiva()).thenReturn(true);
        doNothing().when(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente2).setId(Mockito.<String>any());
        doNothing().when(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente2.setAtiva(false);
        contaCorrente2.setId("Id");
        contaCorrente2.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente2.setSaldo(new BigDecimal("2.3"));
        contaCorrente2.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente2);
        contaCorrenteList.add(contaCorrente);
        when(contaCorrenteRepository.findById(Mockito.<String>any()))
                .thenThrow(new ContaNotFoundException("An error occurred"));
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(ContaNotFoundException.class,
                () -> transferenciaValidationService.validarTransferencia(transferenciaDTO));
        verify(contaCorrente2).getAtiva();
        verify(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente2).setId(eq("Id"));
        verify(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
        verify(contaCorrenteRepository).findById(eq("Id Conta Origem"));
    }

    @Test
    void testValidarTransferencia7() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        ContaCorrente contaCorrente2 = mock(ContaCorrente.class);
        when(contaCorrente2.getAtiva()).thenReturn(true);
        doNothing().when(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente2).setId(Mockito.<String>any());
        doNothing().when(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente2.setAtiva(false);
        contaCorrente2.setId("Id");
        contaCorrente2.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente2.setSaldo(new BigDecimal("2.3"));
        contaCorrente2.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente2);
        contaCorrenteList.add(contaCorrente);
        ContaCorrente contaCorrente3 = mock(ContaCorrente.class);
        when(contaCorrente3.getLimiteDiario()).thenThrow(new ContaInativaException("An error occurred"));
        when(contaCorrente3.getSaldo()).thenReturn(new BigDecimal("2.3"));
        doNothing().when(contaCorrente3).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente3).setId(Mockito.<String>any());
        doNothing().when(contaCorrente3).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente3).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente3).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente3.setAtiva(true);
        contaCorrente3.setId("42");
        contaCorrente3.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente3.setSaldo(new BigDecimal("2.3"));
        contaCorrente3.setTotalTransferidoHoje(new BigDecimal("2.3"));
        Optional<ContaCorrente> ofResult = Optional.of(contaCorrente3);
        when(contaCorrenteRepository.findById(Mockito.<String>any())).thenReturn(ofResult);
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(ContaInativaException.class,
                () -> transferenciaValidationService.validarTransferencia(transferenciaDTO));
        verify(contaCorrente2).getAtiva();
        verify(contaCorrente3).getLimiteDiario();
        verify(contaCorrente3).getSaldo();
        verify(contaCorrente3).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente3).setId(eq("42"));
        verify(contaCorrente2).setId(eq("Id"));
        verify(contaCorrente3).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente3).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente3).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
        verify(contaCorrenteRepository, atLeast(1)).findById(eq("Id Conta Origem"));
    }

    @Test
    void testValidarTransferencia8() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        ContaCorrente contaCorrente2 = mock(ContaCorrente.class);
        when(contaCorrente2.getAtiva()).thenReturn(true);
        doNothing().when(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente2).setId(Mockito.<String>any());
        doNothing().when(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente2.setAtiva(false);
        contaCorrente2.setId("Id");
        contaCorrente2.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente2.setSaldo(new BigDecimal("2.3"));
        contaCorrente2.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente2);
        contaCorrenteList.add(contaCorrente);
        ContaCorrente contaCorrente3 = mock(ContaCorrente.class);
        when(contaCorrente3.getLimiteDiario()).thenReturn(new BigDecimal("5.6"));
        when(contaCorrente3.getTotalTransferidoHoje()).thenReturn(new BigDecimal("2.3"));
        when(contaCorrente3.getSaldo()).thenReturn(new BigDecimal("2.3"));
        doNothing().when(contaCorrente3).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente3).setId(Mockito.<String>any());
        doNothing().when(contaCorrente3).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente3).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente3).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente3.setAtiva(true);
        contaCorrente3.setId("42");
        contaCorrente3.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente3.setSaldo(new BigDecimal("2.3"));
        contaCorrente3.setTotalTransferidoHoje(new BigDecimal("2.3"));
        Optional<ContaCorrente> ofResult = Optional.of(contaCorrente3);
        when(contaCorrenteRepository.findById(Mockito.<String>any())).thenReturn(ofResult);
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act
        transferenciaValidationService.validarTransferencia(transferenciaDTO);

        // Assert that nothing has changed
        verify(contaCorrente2).getAtiva();
        verify(contaCorrente3).getLimiteDiario();
        verify(contaCorrente3).getSaldo();
        verify(contaCorrente3).getTotalTransferidoHoje();
        verify(contaCorrente3).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente3).setId(eq("42"));
        verify(contaCorrente2).setId(eq("Id"));
        verify(contaCorrente3).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente3).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente3).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
        verify(contaCorrenteRepository, atLeast(1)).findById(eq("Id Conta Origem"));
        assertEquals("Id Conta Destino", transferenciaDTO.getIdContaDestino());
        assertEquals("Id Conta Origem", transferenciaDTO.getIdContaOrigem());
        BigDecimal expectedValor = new BigDecimal("2.3");
        assertEquals(expectedValor, transferenciaDTO.getValor());
    }

    @Test
    void testValidarTransferencia9() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        ContaCorrente contaCorrente2 = mock(ContaCorrente.class);
        when(contaCorrente2.getAtiva()).thenReturn(true);
        doNothing().when(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente2).setId(Mockito.<String>any());
        doNothing().when(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente2.setAtiva(false);
        contaCorrente2.setId("Id");
        contaCorrente2.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente2.setSaldo(new BigDecimal("2.3"));
        contaCorrente2.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente2);
        contaCorrenteList.add(contaCorrente);
        ContaCorrente contaCorrente3 = mock(ContaCorrente.class);
        when(contaCorrente3.getLimiteDiario()).thenReturn(new BigDecimal("2.3"));
        when(contaCorrente3.getTotalTransferidoHoje()).thenReturn(null);
        when(contaCorrente3.getSaldo()).thenReturn(new BigDecimal("2.3"));
        doNothing().when(contaCorrente3).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente3).setId(Mockito.<String>any());
        doNothing().when(contaCorrente3).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente3).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente3).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente3.setAtiva(true);
        contaCorrente3.setId("42");
        contaCorrente3.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente3.setSaldo(new BigDecimal("2.3"));
        contaCorrente3.setTotalTransferidoHoje(new BigDecimal("2.3"));
        Optional<ContaCorrente> ofResult = Optional.of(contaCorrente3);
        when(contaCorrenteRepository.findById(Mockito.<String>any())).thenReturn(ofResult);
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act
        transferenciaValidationService.validarTransferencia(transferenciaDTO);

        // Assert that nothing has changed
        verify(contaCorrente2).getAtiva();
        verify(contaCorrente3).getLimiteDiario();
        verify(contaCorrente3).getSaldo();
        verify(contaCorrente3).getTotalTransferidoHoje();
        verify(contaCorrente3).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente3).setId(eq("42"));
        verify(contaCorrente2).setId(eq("Id"));
        verify(contaCorrente3).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente3).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente3).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
        verify(contaCorrenteRepository, atLeast(1)).findById(eq("Id Conta Origem"));
        assertEquals("Id Conta Destino", transferenciaDTO.getIdContaDestino());
        assertEquals("Id Conta Origem", transferenciaDTO.getIdContaOrigem());
        BigDecimal expectedValor = new BigDecimal("2.3");
        assertEquals(expectedValor, transferenciaDTO.getValor());
    }

    @Test
    void testValidarTransferencia10() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        ContaCorrente contaCorrente2 = mock(ContaCorrente.class);
        when(contaCorrente2.getAtiva()).thenReturn(true);
        doNothing().when(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente2).setId(Mockito.<String>any());
        doNothing().when(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente2.setAtiva(false);
        contaCorrente2.setId("Id");
        contaCorrente2.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente2.setSaldo(new BigDecimal("2.3"));
        contaCorrente2.setTotalTransferidoHoje(new BigDecimal("2.3"));

        ArrayList<ContaCorrente> contaCorrenteList = new ArrayList<>();
        contaCorrenteList.add(contaCorrente2);
        contaCorrenteList.add(contaCorrente);
        ContaCorrente contaCorrente3 = mock(ContaCorrente.class);
        when(contaCorrente3.getSaldo()).thenReturn(new BigDecimal("-2.3"));
        doNothing().when(contaCorrente3).setAtiva(Mockito.<Boolean>any());
        doNothing().when(contaCorrente3).setId(Mockito.<String>any());
        doNothing().when(contaCorrente3).setLimiteDiario(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente3).setSaldo(Mockito.<BigDecimal>any());
        doNothing().when(contaCorrente3).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        contaCorrente3.setAtiva(true);
        contaCorrente3.setId("42");
        contaCorrente3.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente3.setSaldo(new BigDecimal("2.3"));
        contaCorrente3.setTotalTransferidoHoje(new BigDecimal("2.3"));
        Optional<ContaCorrente> ofResult = Optional.of(contaCorrente3);
        when(contaCorrenteRepository.findById(Mockito.<String>any())).thenReturn(ofResult);
        when(contaCorrenteRepository.findContasByIds(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(contaCorrenteList);

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
        transferenciaDTO.setIdContaDestino("Id Conta Destino");
        transferenciaDTO.setIdContaOrigem("Id Conta Origem");
        transferenciaDTO.setValor(new BigDecimal("2.3"));

        // Act and Assert
        assertThrows(SaldoInsuficienteException.class,
                () -> transferenciaValidationService.validarTransferencia(transferenciaDTO));
        verify(contaCorrente2).getAtiva();
        verify(contaCorrente3).getSaldo();
        verify(contaCorrente3).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente2).setAtiva(Mockito.<Boolean>any());
        verify(contaCorrente3).setId(eq("42"));
        verify(contaCorrente2).setId(eq("Id"));
        verify(contaCorrente3).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setLimiteDiario(Mockito.<BigDecimal>any());
        verify(contaCorrente3).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setSaldo(Mockito.<BigDecimal>any());
        verify(contaCorrente3).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrente2).setTotalTransferidoHoje(Mockito.<BigDecimal>any());
        verify(contaCorrenteRepository).findContasByIds(eq("Id Conta Origem"), eq("Id Conta Destino"));
        verify(contaCorrenteRepository).findById(eq("Id Conta Origem"));
    }
}
