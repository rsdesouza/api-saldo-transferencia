package br.com.bankdesafio.apisaldotransferencia.rest;

import br.com.bankdesafio.apisaldotransferencia.dto.ClienteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "clienteFeignClient", url = "${cadastro.api.url}")
public interface ClienteFeignClient {

    @GetMapping("/cliente/conta/{id_conta}")
    ClienteResponse getClienteById(@PathVariable("id_conta") UUID idConta);
}
