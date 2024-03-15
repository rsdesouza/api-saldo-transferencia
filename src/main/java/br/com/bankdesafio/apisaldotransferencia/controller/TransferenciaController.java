package br.com.bankdesafio.apisaldotransferencia.controller;


import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<Void> realizarTransferencia(@RequestBody TransferenciaDTO transferenciaDTO) {
        transferenciaService.realizarTransferencia(transferenciaDTO);
        return ResponseEntity.ok().build();
    }

}