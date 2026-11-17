package co.edu.sabana.inventarium.domain.model;

import co.edu.sabana.inventarium.domain.exception.PedidoInvalidoException;

import java.math.BigDecimal;

/** Una linea del pedido: que producto, cuantas unidades y a que precio se vendio. */
public record LineaDePedido(String sku, int cantidad, BigDecimal precioUnitario) {

    public LineaDePedido {
        if (cantidad <= 0) {
            throw new PedidoInvalidoException("La cantidad de " + sku + " debe ser mayor que cero");
        }
    }

    public BigDecimal subtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
