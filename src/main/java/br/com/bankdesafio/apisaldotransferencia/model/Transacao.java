package br.com.bankdesafio.apisaldotransferencia.model;

import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@AllArgsConstructor
@Table(name = "tbl_transacao")
public class Transacao implements Serializable {

    @Serial
    private static final long serialVersionUID = -7941328091536232738L;

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "data_transacao")
    private LocalDateTime dataHoraTransacao; // Corrigido

    @ManyToOne
    @JoinColumn(name = "conta_origem_id", referencedColumnName = "id")
    private ContaCorrente contaOrigem;

    @Column(name = "nome_cliente_origem")
    private String nomeClienteOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id", referencedColumnName = "id")
    private ContaCorrente contaDestino;

    @Column(name = "nome_cliente_destino")
    private String nomeClienteDestino;

    public Transacao() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHoraTransacao() {
        return dataHoraTransacao;
    }

    public void setDataHoraTransacao(LocalDateTime dataHoraTransacao) {
        this.dataHoraTransacao = dataHoraTransacao;
    }

    public ContaCorrente getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(ContaCorrente contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public ContaCorrente getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(ContaCorrente contaDestino) {
        this.contaDestino = contaDestino;
    }

    public String getNomeClienteOrigem() {
        return nomeClienteOrigem;
    }

    public void setNomeClienteOrigem(String nomeClienteOrigem) {
        this.nomeClienteOrigem = nomeClienteOrigem;
    }

    public String getNomeClienteDestino() {
        return nomeClienteDestino;
    }

    public void setNomeClienteDestino(String nomeClienteDestino) {
        this.nomeClienteDestino = nomeClienteDestino;
    }
}
