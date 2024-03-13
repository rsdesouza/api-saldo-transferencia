package br.com.bankdesafio.apisaldotransferencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
public class ClienteResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2028879961365308239L;

    @JsonProperty("id_conta")
    private UUID idConta;

    @JsonProperty("nome")
    private String nome;

    public UUID getIdConta() {
        return idConta;
    }

    public void setIdConta(UUID idConta) {
        this.idConta = idConta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
