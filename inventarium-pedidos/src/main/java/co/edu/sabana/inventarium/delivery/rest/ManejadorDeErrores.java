package co.edu.sabana.inventarium.delivery.rest;

import co.edu.sabana.inventarium.domain.exception.PedidoInvalidoException;
import co.edu.sabana.inventarium.domain.exception.PedidoNoEncontradoException;
import co.edu.sabana.inventarium.domain.exception.ProductoNoEncontradoException;
import co.edu.sabana.inventarium.domain.exception.StockInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Traduce excepciones del dominio a respuestas HTTP (ProblemDetail, RFC 9457). */
@RestControllerAdvice
public class ManejadorDeErrores {

    @ExceptionHandler({PedidoNoEncontradoException.class, ProductoNoEncontradoException.class})
    public ProblemDetail noEncontrado(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ProblemDetail sinStock(StockInsuficienteException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(PedidoInvalidoException.class)
    public ProblemDetail invalido(PedidoInvalidoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
