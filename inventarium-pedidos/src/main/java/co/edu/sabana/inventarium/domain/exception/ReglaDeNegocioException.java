package co.edu.sabana.inventarium.domain.exception;

/**
 * Raiz de las excepciones del dominio. Es Java puro: el dominio no sabe que
 * existe HTTP. Traducir estas excepciones a codigos de estado es trabajo del
 * adaptador primario (delivery/rest).
 */
public abstract class ReglaDeNegocioException extends RuntimeException {

    protected ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
