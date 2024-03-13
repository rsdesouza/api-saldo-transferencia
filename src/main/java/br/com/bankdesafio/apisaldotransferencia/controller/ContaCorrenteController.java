package br.com.bankdesafio.apisaldotransferencia.controller;

import br.com.bankdesafio.apisaldotransferencia.dto.SaldoDTO;
import br.com.bankdesafio.apisaldotransferencia.service.ContaCorrenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/contas")
public class ContaCorrenteController {

    @Autowired
    private ContaCorrenteService contaCorrenteService;

    @GetMapping("/{id}/saldo")
    public ResponseEntity<SaldoDTO> consultarSaldo(@PathVariable UUID id) {
        SaldoDTO saldo = contaCorrenteService.consultarSaldo(id);
        return ResponseEntity.ok(saldo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarConta(@PathVariable UUID id) {
        contaCorrenteService.desativarConta(id);
        return ResponseEntity.ok().build();
    }

}