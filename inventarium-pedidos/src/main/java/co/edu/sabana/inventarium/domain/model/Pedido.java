package co.edu.sabana.inventarium.domain.model;

import co.edu.sabana.inventarium.domain.exception.PedidoInvalidoException;

import java.math.BigDecimal;
import java.util.List;

/** Pedido de un cliente de la distribuidora. Agregado raiz del dominio. */
public class Pedido {

    private final String id;
    private final String cliente;
    private final List<LineaDePedido> lineas;
    private EstadoPedido estado;

    private Pedido(String id, String cliente, List<LineaDePedido> lineas) {
        this.id = id;
        this.cliente = cliente;
        this.lineas = List.copyOf(lineas);
        this.estado = EstadoPedido.CREADO;
    }

    public static Pedido crear(String id, String cliente, List<LineaDePedido> lineas) {
        if (cliente == null || cliente.isBlank()) {
            throw new PedidoInvalidoException("El pedido debe tener un cliente");
        }
        if (lineas == null || lineas.isEmpty()) {
            throw new PedidoInvalidoException("El pedido debe tener al menos una linea");
        }
        long skusDistintos = lineas.stream().map(LineaDePedido::sku).distinct().count();
        if (skusDistintos != lineas.size()) {
            throw new PedidoInvalidoException("El pedido no puede repetir un SKU en dos lineas");
        }
        return new Pedido(id, cliente, lineas);
    }

    public BigDecimal total() {
        return lineas.stream()
                .map(LineaDePedido::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void despachar() {
        if (estado == EstadoPedido.DESPACHADO) {
            throw new PedidoInvalidoException("El pedido " + id + " ya fue despachado");
        }
        estado = EstadoPedido.DESPACHADO;
    }

    public String id() { return id; }
    public String cliente() { return cliente; }
    public List<LineaDePedido> lineas() { return lineas; }
    public EstadoPedido estado() { return estado; }
}
