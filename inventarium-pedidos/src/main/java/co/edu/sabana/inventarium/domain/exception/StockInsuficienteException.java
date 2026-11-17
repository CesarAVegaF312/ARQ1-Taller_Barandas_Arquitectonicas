package co.edu.sabana.inventarium.domain.exception;

public class StockInsuficienteException extends ReglaDeNegocioException {

    public StockInsuficienteException(String sku, int solicitado, int disponible) {
        super("Stock insuficiente para " + sku + ": se pidieron " + solicitado
                + " y hay " + disponible);
    }
}
