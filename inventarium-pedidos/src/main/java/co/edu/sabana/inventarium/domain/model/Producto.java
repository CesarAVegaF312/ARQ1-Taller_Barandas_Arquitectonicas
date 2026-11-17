package co.edu.sabana.inventarium.domain.model;

import co.edu.sabana.inventarium.domain.exception.PedidoInvalidoException;
import co.edu.sabana.inventarium.domain.exception.StockInsuficienteException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Producto del catalogo de Inventarium. Entidad del dominio: sin anotaciones
 * de Spring, de JPA ni de Jackson.
 */
public class Producto {

    private final String sku;
    private final String nombre;
    private final BigDecimal precioUnitario;
    private int stock;

    public Producto(String sku, String nombre, BigDecimal precioUnitario, int stock) {
        if (sku == null || sku.isBlank()) {
            throw new PedidoInvalidoException("El SKU es obligatorio");
        }
        if (precioUnitario == null || precioUnitario.signum() < 0) {
            throw new PedidoInvalidoException("El precio de " + sku + " no puede ser negativo");
        }
        if (stock < 0) {
            throw new PedidoInvalidoException("El stock de " + sku + " no puede ser negativo");
        }
        this.sku = sku;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
    }

    public boolean puedeSurtir(int cantidad) {
        return cantidad > 0 && cantidad <= stock;
    }

    public void descontarStock(int cantidad) {
        if (!puedeSurtir(cantidad)) {
            throw new StockInsuficienteException(sku, cantidad, stock);
        }
        stock -= cantidad;
    }

    public String sku() { return sku; }
    public String nombre() { return nombre; }
    public BigDecimal precioUnitario() { return precioUnitario; }
    public int stock() { return stock; }

    @Override
    public boolean equals(Object o) {
        return o instanceof Producto otro && sku.equals(otro.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }
}
