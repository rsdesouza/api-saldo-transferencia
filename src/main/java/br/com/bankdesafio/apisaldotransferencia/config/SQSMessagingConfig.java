package br.com.bankdesafio.apisaldotransferencia.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SQSMessagingConfig {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${localstack.url}")
    private String localstackUrl;

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(localstackUrl, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("test", "test")))
                .build();
    }

    @Bean
    @Primary
    public QueueMessagingTemplate queueMessagingTemplate(){
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

}
