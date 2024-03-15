package br.com.bankdesafio.apisaldotransferencia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import br.com.bankdesafio.apisaldotransferencia.dto.NotificacaoBacenDTO;
import br.com.bankdesafio.apisaldotransferencia.messaging.SqsMessageSender;
import br.com.bankdesafio.apisaldotransferencia.rest.BACENFeignClient;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {BACENNotificationService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class BACENNotificationServiceTest {
    @MockBean
    private BACENFeignClient bACENFeignClient;

    @Autowired
    private BACENNotificationService bACENNotificationService;

    @MockBean
    private SqsMessageSender sqsMessageSender;

    @Test
    void testNotificarBACEN() {
        // Arrange
        doNothing().when(bACENFeignClient).notificarTransacao(Mockito.<NotificacaoBacenDTO>any());
        NotificacaoBacenDTO notificacaoBacenDTO = new NotificacaoBacenDTO("e8fe502e-1c1f-4585-8ddb-43449abf8562", new BigDecimal("33.50"), "ec459c2f-d7ca-41af-8abe-87967d947a13",
                "a07da740-429f-431e-9474-8a070ed54ceb");

        // Act
        bACENNotificationService.notificarBACEN(notificacaoBacenDTO);

        // Assert that nothing has changed
        verify(bACENFeignClient).notificarTransacao(Mockito.<NotificacaoBacenDTO>any());
        assertEquals("e8fe502e-1c1f-4585-8ddb-43449abf8562", notificacaoBacenDTO.getTransacaoId());
        assertEquals("a07da740-429f-431e-9474-8a070ed54ceb", notificacaoBacenDTO.getContaDestino());
        assertEquals("ec459c2f-d7ca-41af-8abe-87967d947a13", notificacaoBacenDTO.getContaOrigem());
        BigDecimal expectedValor = new BigDecimal("33.50");
        assertEquals(expectedValor, notificacaoBacenDTO.getValor());
    }
}
