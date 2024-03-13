package br.com.bankdesafio.apisaldotransferencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
public class TransferenciaDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9080491016456834683L;

    @JsonProperty("id_conta_origem")
    private UUID idContaOrigem;

    @JsonProperty("id_conta_destino")
    private UUID idContaDestino;

    @JsonProperty("valor")
    private BigDecimal valor;

    public TransferenciaDTO() {

    }

    public UUID getIdContaOrigem() {
        return idContaOrigem;
    }

    public void setIdContaOrigem(UUID idContaOrigem) {
        this.idContaOrigem = idContaOrigem;
    }

    public UUID getIdContaDestino() {
        return idContaDestino;
    }

    public void setIdContaDestino(UUID idContaDestino) {
        this.idContaDestino = idContaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
