package co.edu.sabana.inventarium.domain.exception;

public class PedidoNoEncontradoException extends ReglaDeNegocioException {

    public PedidoNoEncontradoException(String id) {
        super("No existe el pedido " + id);
    }
}
