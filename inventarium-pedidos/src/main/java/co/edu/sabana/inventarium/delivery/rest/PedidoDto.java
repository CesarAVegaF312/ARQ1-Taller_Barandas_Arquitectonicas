package co.edu.sabana.inventarium.delivery.rest;

import co.edu.sabana.inventarium.domain.model.Pedido;

import java.math.BigDecimal;
import java.util.List;

/** Respuesta JSON. Traduce el modelo del dominio al contrato HTTP. */
public record PedidoDto(String id, String cliente, String estado, BigDecimal total, List<LineaDto> lineas) {

    public record LineaDto(String sku, int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
    }

    static PedidoDto desde(Pedido pedido) {
        List<LineaDto> lineas = pedido.lineas().stream()
                .map(l -> new LineaDto(l.sku(), l.cantidad(), l.precioUnitario(), l.subtotal()))
                .toList();
        return new PedidoDto(pedido.id(), pedido.cliente(), pedido.estado().name(), pedido.total(), lineas);
    }
}
