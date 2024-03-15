package br.com.bankdesafio.apisaldotransferencia.service.validation;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.bankdesafio.apisaldotransferencia.exception.ContaInativaException;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ContaCorrenteValidationService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ContaCorrenteValidationServiceTest {
    @MockBean
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private ContaCorrenteValidationService contaCorrenteValidationService;

    @Test
    void testValidarContaAtiva() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        Optional<ContaCorrente> ofResult = Optional.of(contaCorrente);
        when(contaCorrenteRepository.findContaAtivaById(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        ContaCorrente actualValidarContaAtivaResult = contaCorrenteValidationService.validarContaAtiva("42");

        // Assert
        verify(contaCorrenteRepository).findContaAtivaById(eq("42"));
        assertSame(contaCorrente, actualValidarContaAtivaResult);
    }

    @Test
    void testValidarContaAtiva2() {
        // Arrange
        Optional<ContaCorrente> emptyResult = Optional.empty();
        when(contaCorrenteRepository.findContaAtivaById(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ContaInativaException.class, () -> contaCorrenteValidationService.validarContaAtiva("42"));
        verify(contaCorrenteRepository).findContaAtivaById(eq("42"));
    }

    @Test
    void testValidarContaAtiva3() {
        // Arrange
        when(contaCorrenteRepository.findContaAtivaById(Mockito.<String>any()))
                .thenThrow(new IllegalArgumentException("Conta corrente não encontrada ou inativa."));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> contaCorrenteValidationService.validarContaAtiva("42"));
        verify(contaCorrenteRepository).findContaAtivaById(eq("42"));
    }

    @Test
    void testValidarExistenciaConta() {
        // Arrange
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setAtiva(true);
        contaCorrente.setId("42");
        contaCorrente.setLimiteDiario(new BigDecimal("2.3"));
        contaCorrente.setSaldo(new BigDecimal("2.3"));
        contaCorrente.setTotalTransferidoHoje(new BigDecimal("2.3"));
        Optional<ContaCorrente> ofResult = Optional.of(contaCorrente);
        when(contaCorrenteRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        ContaCorrente actualValidarExistenciaContaResult = contaCorrenteValidationService.validarExistenciaConta("42");

        // Assert
        verify(contaCorrenteRepository).findById(eq("42"));
        assertSame(contaCorrente, actualValidarExistenciaContaResult);
    }

    @Test
    void testValidarExistenciaConta2() {
        // Arrange
        Optional<ContaCorrente> emptyResult = Optional.empty();
        when(contaCorrenteRepository.findById(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> contaCorrenteValidationService.validarExistenciaConta("42"));
        verify(contaCorrenteRepository).findById(eq("42"));
    }

    @Test
    void testValidarExistenciaConta3() {
        // Arrange
        when(contaCorrenteRepository.findById(Mockito.<String>any()))
                .thenThrow(new IllegalArgumentException("Conta corrente não encontrada."));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> contaCorrenteValidationService.validarExistenciaConta("42"));
        verify(contaCorrenteRepository).findById(eq("42"));
    }
}
