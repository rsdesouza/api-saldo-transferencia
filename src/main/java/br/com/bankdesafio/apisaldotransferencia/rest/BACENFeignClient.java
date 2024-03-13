package br.com.bankdesafio.apisaldotransferencia.rest;

import br.com.bankdesafio.apisaldotransferencia.dto.NotificacaoBacenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bacenFeignClient", url = "${bacen.api.url}")
public interface BACENFeignClient {

    @PostMapping("/transacao/notificar")
    ResponseEntity<Void> notificarTransacao(@RequestBody NotificacaoBacenDTO notificacaoBacenDTO);

}
