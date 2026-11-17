package co.edu.sabana.inventarium.application.usecase;

import co.edu.sabana.inventarium.application.port.in.ConsultarPedidoCasoDeUso;
import co.edu.sabana.inventarium.application.port.out.RepositorioDePedidos;
import co.edu.sabana.inventarium.domain.exception.PedidoNoEncontradoException;
import co.edu.sabana.inventarium.domain.model.Pedido;

public class ConsultarPedidoServicio implements ConsultarPedidoCasoDeUso {

    private final RepositorioDePedidos pedidos;

    public ConsultarPedidoServicio(RepositorioDePedidos pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public Pedido porId(String id) {
        return pedidos.buscarPorId(id).orElseThrow(() -> new PedidoNoEncontradoException(id));
    }
}
