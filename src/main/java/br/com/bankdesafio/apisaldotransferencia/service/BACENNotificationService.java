package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.NotificacaoBacenDTO;
import br.com.bankdesafio.apisaldotransferencia.rest.BACENFeignClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BACENNotificationService {

    private final BACENFeignClient bacenFeignClient;

    @Autowired
    public BACENNotificationService(BACENFeignClient bacenFeignClient) {
        this.bacenFeignClient = bacenFeignClient;
    }

    public void notificarBACEN(NotificacaoBacenDTO notificacaoBacenDTO) {

        // Enviar a notificação para o BACEN
        try {
            ResponseEntity<Void> response = bacenFeignClient.notificarTransacao(notificacaoBacenDTO);
            if (!response.getStatusCode().is2xxSuccessful()) {
                // Tratar a resposta não bem-sucedida conforme necessário
                // Por exemplo, pode-se logar o incidente, tentar novamente, etc.
                System.err.println("Falha ao notificar o BACEN sobre a transação.");
            }
        } catch (FeignException e) {
            // Tratar exceções, por exemplo, limitação de taxa
            if (e.status() == 429) {
                System.err.println("Limite de notificações ao BACEN excedido. Tente novamente mais tarde.");
            } else {
                System.err.println("Erro ao comunicar com o BACEN: " + e.getMessage());
            }
        }
    }
}
