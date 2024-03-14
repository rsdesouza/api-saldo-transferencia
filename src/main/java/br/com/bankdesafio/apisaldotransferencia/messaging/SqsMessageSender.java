package br.com.bankdesafio.apisaldotransferencia.messaging;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsMessageSender {

    private final AmazonSQS sqsClient;

    @Value("${url.sqs.queue}")
    private String queueUrl;

    public SqsMessageSender() {
        this.sqsClient = AmazonSQSClientBuilder.standard()
                .withRegion(Regions.DEFAULT_REGION)
                .build();
    }

    public void sendMessage(String messageBody) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);
        sqsClient.sendMessage(send_msg_request);
    }
}
