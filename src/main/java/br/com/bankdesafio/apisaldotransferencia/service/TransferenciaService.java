package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.NotificacaoBacenDTO;
import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.model.Transacao;
import br.com.bankdesafio.apisaldotransferencia.service.validation.TransferenciaValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferenciaService {

    private final ContaCorrenteService contaCorrenteService;
    private final ClienteService clienteService;
    private final TransacaoService transacaoService;
    private final BACENNotificationService bacenNotificationService;
    private final TransferenciaValidationService transferenciaValidationService;

    @Autowired
    public TransferenciaService(ContaCorrenteService contaCorrenteService, ClienteService clienteService,
                                TransacaoService transacaoService, BACENNotificationService bacenNotificationService,
                                TransferenciaValidationService transferenciaValidationService) {
        this.contaCorrenteService = contaCorrenteService;
        this.clienteService = clienteService;
        this.transacaoService = transacaoService;
        this.bacenNotificationService = bacenNotificationService;
        this.transferenciaValidationService = transferenciaValidationService;
    }

    @Transactional
    public void realizarTransferencia(TransferenciaDTO transferenciaDTO) {
        // Validar pré-requisitos da transferência
        transferenciaValidationService.validarTransferencia(transferenciaDTO);

        // Atualizar saldos das contas correntes envolvidas
        ContaCorrente contaCorrente = contaCorrenteService.atualizarSaldo(transferenciaDTO);

        //Atualizar total transferido hoje
        contaCorrenteService.atualizarTotalTransferidoHoje(contaCorrente, transferenciaDTO.getValor());

        // Obter informações dos clientes envolvidos
        var clienteOrigem = clienteService.getClienteById(transferenciaDTO.getIdContaOrigem());
        var clienteDestino = clienteService.getClienteById(transferenciaDTO.getIdContaDestino());

        // Criar e registrar a transação
        Transacao transacao = transacaoService.criarTransacao(transferenciaDTO, clienteOrigem.getNome(), clienteDestino.getNome());

        NotificacaoBacenDTO notificacaoBacenDTO = new NotificacaoBacenDTO(
                transacao.getId(),
                transacao.getValor(),
                transacao.getContaOrigem().getId(),
                transacao.getContaDestino().getId()
        );

        // Notificar o BACEN sobre a conclusão da transferência
        bacenNotificationService.notificarBACEN(notificacaoBacenDTO);
    }
}
