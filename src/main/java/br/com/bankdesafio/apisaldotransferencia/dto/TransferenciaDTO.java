package br.com.bankdesafio.apisaldotransferencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
public class TransferenciaDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9080491016456834683L;

    @JsonProperty("id_conta_origem")
    private String idContaOrigem;

    @JsonProperty("id_conta_destino")
    private String idContaDestino;

    @JsonProperty("valor")
    private BigDecimal valor;

    public TransferenciaDTO() {

    }

    public String getIdContaOrigem() {
        return idContaOrigem;
    }

    public void setIdContaOrigem(String idContaOrigem) {
        this.idContaOrigem = idContaOrigem;
    }

    public String getIdContaDestino() {
        return idContaDestino;
    }

    public void setIdContaDestino(String idContaDestino) {
        this.idContaDestino = idContaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
