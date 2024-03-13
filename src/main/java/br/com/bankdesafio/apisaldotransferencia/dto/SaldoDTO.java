package br.com.bankdesafio.apisaldotransferencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
public class SaldoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7941328091536232738L;

    @JsonProperty("id_conta")
    private UUID idConta;

    @JsonProperty("saldo")
    private BigDecimal saldo;

    public SaldoDTO() {

    }

    public UUID getIdConta() {
        return idConta;
    }

    public void setIdConta(UUID idConta) {
        this.idConta = idConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
