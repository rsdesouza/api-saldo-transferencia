package br.com.bankdesafio.apisaldotransferencia.service.validation;

import br.com.bankdesafio.apisaldotransferencia.exception.ContaInativaException;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContaCorrenteValidationService {

    private final ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    public ContaCorrenteValidationService(ContaCorrenteRepository contaCorrenteRepository) {
        this.contaCorrenteRepository = contaCorrenteRepository;
    }

    public ContaCorrente validarContaAtiva(String id) {
        return contaCorrenteRepository.findContaAtivaById(id)
                .orElseThrow(() -> new ContaInativaException("Conta corrente não encontrada ou inativa."));
    }

    public ContaCorrente validarExistenciaConta(String id) {
        return contaCorrenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta corrente não encontrada."));
    }

}
