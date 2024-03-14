package br.com.bankdesafio.apisaldotransferencia.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tbl_conta_corrente")
@AllArgsConstructor
public class ContaCorrente implements Serializable  {

    @Serial
    private static final long serialVersionUID = -7941328091536232738L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "saldo")
    private BigDecimal saldo;

    @Column(name = "ativa")
    private Boolean ativa;

    @Column(name = "limite_diario", nullable = false)
    private BigDecimal limiteDiario = new BigDecimal("1000");

    @Column(name = "total_transferido_hoje")
    private BigDecimal totalTransferidoHoje;

    public BigDecimal getTotalTransferidoHoje() {
        return totalTransferidoHoje;
    }

    public void setTotalTransferidoHoje(BigDecimal totalTransferidoHoje) {
        this.totalTransferidoHoje = totalTransferidoHoje;
    }

    public ContaCorrente() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public BigDecimal getLimiteDiario() {
        return limiteDiario;
    }

    public void setLimiteDiario(BigDecimal limiteDiario) {
        this.limiteDiario = limiteDiario;
    }

}
