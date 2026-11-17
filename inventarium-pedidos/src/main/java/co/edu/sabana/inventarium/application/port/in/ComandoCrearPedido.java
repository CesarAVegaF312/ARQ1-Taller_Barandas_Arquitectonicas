package co.edu.sabana.inventarium.application.port.in;

import java.util.List;

/** Datos de entrada del caso de uso. No depende de HTTP ni de JSON. */
public record ComandoCrearPedido(String cliente, List<Item> items) {

    public record Item(String sku, int cantidad) {
    }
}
