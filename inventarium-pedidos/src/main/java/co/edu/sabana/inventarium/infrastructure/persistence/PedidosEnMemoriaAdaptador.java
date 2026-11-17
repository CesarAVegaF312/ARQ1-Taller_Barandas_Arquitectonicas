package co.edu.sabana.inventarium.infrastructure.persistence;

import co.edu.sabana.inventarium.application.port.out.RepositorioDePedidos;
import co.edu.sabana.inventarium.domain.model.Pedido;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador secundario: implementa el puerto desde afuera. Cambiarlo por uno
 * con JPA o JDBC no exige tocar ni el dominio ni los casos de uso.
 */
@Component
public class PedidosEnMemoriaAdaptador implements RepositorioDePedidos {

    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();

    @Override
    public Pedido guardar(Pedido pedido) {
        pedidos.put(pedido.id(), pedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return Optional.ofNullable(pedidos.get(id));
    }
}
