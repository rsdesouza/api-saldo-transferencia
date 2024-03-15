package br.com.bankdesafio.apisaldotransferencia.exception;

public class ContaNotFoundException extends RuntimeException{
    public ContaNotFoundException(String message) {
        super(message);
    }

    public ContaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
