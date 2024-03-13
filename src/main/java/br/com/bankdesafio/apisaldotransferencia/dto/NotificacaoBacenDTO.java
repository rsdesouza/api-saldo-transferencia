package br.com.bankdesafio.apisaldotransferencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class NotificacaoBacenDTO implements Serializable {
    public NotificacaoBacenDTO(String transacaoId, BigDecimal valor, String contaOrigem, String contaDestino) {
        this.transacaoId = transacaoId;
        this.valor = valor;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
    }

    @Serial
    private static final long serialVersionUID = -2028879961365308239L;

    @JsonProperty("transacao_id")
    private String transacaoId;

    @JsonProperty("valor")
    private BigDecimal valor;

    @JsonProperty("conta_origem")
    private String contaOrigem;

    @JsonProperty("conta_destino")
    private String contaDestino;

    public String getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(String transacaoId) {
        this.transacaoId = transacaoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(String contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }
}
