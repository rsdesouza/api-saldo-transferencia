package br.com.bankdesafio.apisaldotransferencia.messaging;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsMessageSender {

    private final AmazonSQS sqsClient;
    private final String queueUrl;

    @Autowired
    public SqsMessageSender(AmazonSQS sqsClient, @Value("${url.sqs.queue}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
    }

    public void sendMessage(String messageBody) {
        SendMessageRequest sendMsgRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);
        sqsClient.sendMessage(sendMsgRequest);
    }
}