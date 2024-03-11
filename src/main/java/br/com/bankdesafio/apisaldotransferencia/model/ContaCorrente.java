package br.com.bankdesafio.apisaldotransferencia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tbl_conta_corrente")
@Data
@AllArgsConstructor
public class ContaCorrente {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome_cliente")
    private String nomeCliente;

    @Column(name = "saldo")
    private BigDecimal saldo;

    @Column(name = "ativa")
    private Boolean ativa;


}
