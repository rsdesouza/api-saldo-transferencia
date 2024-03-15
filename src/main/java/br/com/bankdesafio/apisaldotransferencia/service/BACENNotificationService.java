package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.NotificacaoBacenDTO;
import br.com.bankdesafio.apisaldotransferencia.messaging.SqsMessageSender;
import br.com.bankdesafio.apisaldotransferencia.rest.BACENFeignClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BACENNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BACENNotificationService.class);

    private final SqsMessageSender sqsMessageSender;
    private final BACENFeignClient bacenFeignClient;

    @Autowired
    public BACENNotificationService(BACENFeignClient bacenFeignClient, SqsMessageSender sqsMessageSender) {
        this.bacenFeignClient = bacenFeignClient;
        this.sqsMessageSender = sqsMessageSender;
    }

    public void notificarBACEN(NotificacaoBacenDTO notificacaoBacenDTO) {
        try {
            bacenFeignClient.notificarTransacao(notificacaoBacenDTO);
            LOGGER.info("Notificação enviada ao BACEN com sucesso.");
        } catch (FeignException e) {
            sqsMessageSender.sendMessage(notificacaoBacenDTO.toString());
            LOGGER.error("Falha ao notificar o BACEN: {}", e.getMessage(), e);
        }
    }
}
