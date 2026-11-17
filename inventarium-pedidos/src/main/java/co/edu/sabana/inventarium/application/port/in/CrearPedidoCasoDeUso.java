package co.edu.sabana.inventarium.application.port.in;

import co.edu.sabana.inventarium.domain.model.Pedido;

/**
 * Puerto primario (driving): lo que el sistema ofrece hacia afuera.
 * Lo invoca un controlador REST, una prueba o, manana, un consumidor de eventos.
 */
public interface CrearPedidoCasoDeUso {

    Pedido crear(ComandoCrearPedido comando);
}
