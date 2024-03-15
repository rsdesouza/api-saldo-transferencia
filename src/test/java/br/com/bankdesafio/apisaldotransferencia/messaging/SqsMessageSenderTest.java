package br.com.bankdesafio.apisaldotransferencia.messaging;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SqsMessageSender.class, String.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class SqsMessageSenderTest {
    @MockBean
    private AmazonSQS amazonSQS;

    @Autowired
    private SqsMessageSender sqsMessageSender;

    @Test
    void testSendMessage() {
        // Arrange
        when(amazonSQS.sendMessage(Mockito.<SendMessageRequest>any())).thenReturn(new SendMessageResult());

        // Act
        sqsMessageSender.sendMessage("Not all who wander are lost");

        // Assert
        verify(amazonSQS).sendMessage(Mockito.<SendMessageRequest>any());
    }
}
