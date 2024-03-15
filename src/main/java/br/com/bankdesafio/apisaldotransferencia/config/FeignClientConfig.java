package br.com.bankdesafio.apisaldotransferencia.config;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Logger.Level feignLoggerLevel() {
        // Configura o nível de log para FULL para obter detalhes completos das requisições e respostas
        return Logger.Level.FULL;
    }

    @Bean
    public Encoder feignEncoder() {
        // Utiliza o SpringEncoder para codificar as requisições
        return new SpringEncoder(messageConverters);
    }

    @Bean
    public Decoder feignDecoder() {
        // Utiliza o SpringDecoder para decodificar as respostas
        return new SpringDecoder(messageConverters);
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        // Define um ErrorDecoder personalizado para tratar erros de maneira customizada
        return new CustomErrorDecoder();
    }

    // Implementação do CustomErrorDecoder
    public static class CustomErrorDecoder implements ErrorDecoder {

        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            // Implementação da lógica de tratamento de erro
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
