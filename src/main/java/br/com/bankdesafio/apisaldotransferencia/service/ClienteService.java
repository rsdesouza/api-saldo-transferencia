package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.ClienteResponse;
import br.com.bankdesafio.apisaldotransferencia.rest.BACENFeignClient;
import br.com.bankdesafio.apisaldotransferencia.rest.ClienteFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteFeignClient clienteFeignClient;

    @Autowired
    public ClienteService(ClienteFeignClient clienteFeignClient) {
        this.clienteFeignClient = clienteFeignClient;
    }

    public ClienteResponse getClienteById(UUID idConta) {
        return clienteFeignClient.getClienteById(idConta);
    }
}
