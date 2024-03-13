package br.com.bankdesafio.apisaldotransferencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificacaoBacenDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2028879961365308239L;

    @JsonProperty("id_transacao")
    private UUID idTransacao;

    @JsonProperty("id_conta_origem")
    private UUID idContaOrigem;

    @JsonProperty("id_conta_destino")
    private UUID idContaDestino;

    @JsonProperty("valor")
    private BigDecimal valor;

}
