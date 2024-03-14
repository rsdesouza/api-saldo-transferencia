package br.com.bankdesafio.apisaldotransferencia.exception;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }

    public SaldoInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }
}
