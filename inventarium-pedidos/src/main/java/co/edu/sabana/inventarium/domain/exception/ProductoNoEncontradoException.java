package co.edu.sabana.inventarium.domain.exception;

public class ProductoNoEncontradoException extends ReglaDeNegocioException {

    public ProductoNoEncontradoException(String sku) {
        super("No existe el producto " + sku);
    }
}
