package co.edu.sabana.inventarium.application.port.out;

import co.edu.sabana.inventarium.domain.model.Pedido;

import java.util.Optional;

/**
 * Puerto secundario (driven): lo que el negocio necesita del mundo.
 * Se nombra con el vocabulario del negocio, no con el de la tecnologia
 * (no es un JpaRepository ni un PedidoDao).
 */
public interface RepositorioDePedidos {

    Pedido guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(String id);
}
