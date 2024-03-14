package br.com.bankdesafio.apisaldotransferencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
public class ClienteResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2028879961365308239L;

    @JsonProperty("id_conta")
    private String idConta;

    @JsonProperty("nome")
    private String nome;

    public String getIdConta() {
        return idConta;
    }

    public void setIdConta(String idConta) {
        this.idConta = idConta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
