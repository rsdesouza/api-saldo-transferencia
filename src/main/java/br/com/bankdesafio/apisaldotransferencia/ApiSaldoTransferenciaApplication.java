package br.com.bankdesafio.apisaldotransferencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiSaldoTransferenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSaldoTransferenciaApplication.class, args);
	}

}
