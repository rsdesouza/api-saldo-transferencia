package br.com.bankdesafio.apisaldotransferencia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<?> clienteNotFoundException(ClienteNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("Resource Not Found", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ContaNotFoundException.class)
    public ResponseEntity<?> contaNotFoundException(ContaNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("Resource Not Found", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ContaInativaException.class)
    public ResponseEntity<?> contaInativaException(ContaInativaException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("Invalid Request", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LimiteDiarioExcedidoException.class)
    public ResponseEntity<?> limiteDiarioExcedidoException(LimiteDiarioExcedidoException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("Invalid Request", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<?> saldoInsuficienteException(SaldoInsuficienteException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("Invalid Request", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("Error occurred", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

class ErrorDetails {
    private String title;
    private String message;

    // Constructors, Getters and Setters
    public ErrorDetails(String title, String message) {
        this.title = title;
        this.message = message;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
