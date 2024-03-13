package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.mapper.TransferenciaMapper;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.model.Transacao;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import br.com.bankdesafio.apisaldotransferencia.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransferenciaService {

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private TransferenciaMapper transferenciaMapper;

    @Transactional
    public void realizarTransferencia(TransferenciaDTO transferenciaDTO) {
        validarTransferencia(transferenciaDTO);

        ContaCorrente contaOrigem = contaCorrenteRepository.findById(transferenciaDTO.getIdContaOrigem())
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));
        ContaCorrente contaDestino = contaCorrenteRepository.findById(transferenciaDTO.getIdContaDestino())
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));

        BigDecimal novoSaldoOrigem = contaOrigem.getSaldo().subtract(transferenciaDTO.getValor());
        BigDecimal novoSaldoDestino = contaDestino.getSaldo().add(transferenciaDTO.getValor());
        BigDecimal novoLimiteDiarioOrigem = contaOrigem.getLimiteDiario().subtract(transferenciaDTO.getValor());

        contaOrigem.setSaldo(novoSaldoOrigem);
        contaOrigem.setLimiteDiario(novoLimiteDiarioOrigem);
        contaDestino.setSaldo(novoSaldoDestino);

        contaCorrenteRepository.save(contaOrigem);
        contaCorrenteRepository.save(contaDestino);

        Transacao transacao = transferenciaMapper.toEntity(transferenciaDTO);
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setDataHoraTransacao(LocalDateTime.now());
        transacaoRepository.save(transacao);

        // Notificar BACEN aqui (mock ou implementação real)
    }

    private void validarTransferencia(TransferenciaDTO transferenciaDTO) {
        if (transferenciaDTO.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo.");
        }

        Optional<ContaCorrente> contaOrigemOpt = contaCorrenteRepository.findById(transferenciaDTO.getIdContaOrigem());
        Optional<ContaCorrente> contaDestinoOpt = contaCorrenteRepository.findById(transferenciaDTO.getIdContaDestino());

        contaOrigemOpt.orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));
        contaDestinoOpt.orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));

        ContaCorrente contaOrigem = contaOrigemOpt.get();
        ContaCorrente contaDestino = contaDestinoOpt.get();

        if (!contaOrigem.getAtiva() || !contaDestino.getAtiva()) {
            throw new IllegalArgumentException("Conta de origem ou destino inativa.");
        }

        if (contaOrigem.getSaldo().compareTo(transferenciaDTO.getValor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência.");
        }

        if (contaOrigem.getLimiteDiario().compareTo(transferenciaDTO.getValor()) < 0) {
            throw new IllegalArgumentException("Valor da transferência excede o limite diário permitido.");
        }
    }
}
