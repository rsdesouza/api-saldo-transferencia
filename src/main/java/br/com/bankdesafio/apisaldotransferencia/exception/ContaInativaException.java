package br.com.bankdesafio.apisaldotransferencia.exception;

public class ContaInativaException extends RuntimeException {

    public ContaInativaException(String message) {
        super(message);
    }

    public ContaInativaException(String message, Throwable cause) {
        super(message, cause);
    }
}
