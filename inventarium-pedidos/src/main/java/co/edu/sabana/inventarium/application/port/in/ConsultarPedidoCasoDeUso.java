package co.edu.sabana.inventarium.application.port.in;

import co.edu.sabana.inventarium.domain.model.Pedido;

/** Puerto primario (driving) para consultar un pedido. */
public interface ConsultarPedidoCasoDeUso {

    Pedido porId(String id);
}
