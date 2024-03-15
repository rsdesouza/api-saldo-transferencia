package br.com.bankdesafio.apisaldotransferencia.exception;

public class LimiteDiarioExcedidoException extends RuntimeException {

    public LimiteDiarioExcedidoException(String message) {
        super(message);
    }

    public LimiteDiarioExcedidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
