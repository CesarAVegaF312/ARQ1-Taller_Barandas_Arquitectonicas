package co.edu.sabana.inventarium.application.usecase;

import co.edu.sabana.inventarium.application.port.in.ComandoCrearPedido;
import co.edu.sabana.inventarium.application.port.in.CrearPedidoCasoDeUso;
import co.edu.sabana.inventarium.application.port.out.CatalogoDeProductos;
import co.edu.sabana.inventarium.application.port.out.RepositorioDePedidos;
import co.edu.sabana.inventarium.domain.exception.PedidoInvalidoException;
import co.edu.sabana.inventarium.domain.exception.ProductoNoEncontradoException;
import co.edu.sabana.inventarium.domain.exception.StockInsuficienteException;
import co.edu.sabana.inventarium.domain.model.LineaDePedido;
import co.edu.sabana.inventarium.domain.model.Pedido;
import co.edu.sabana.inventarium.domain.model.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementa el puerto primario usando solo puertos secundarios.
 * No lleva @Service: la aplicacion es Java puro y se cablea en config/.
 */
public class CrearPedidoServicio implements CrearPedidoCasoDeUso {

    private final CatalogoDeProductos catalogo;
    private final RepositorioDePedidos pedidos;

    public CrearPedidoServicio(CatalogoDeProductos catalogo, RepositorioDePedidos pedidos) {
        this.catalogo = catalogo;
        this.pedidos = pedidos;
    }

    @Override
    public Pedido crear(ComandoCrearPedido comando) {
        if (comando.items() == null || comando.items().isEmpty()) {
            throw new PedidoInvalidoException("El pedido debe tener al menos una linea");
        }

        // 1. Validar todo antes de tocar el stock: o se surte el pedido completo o nada.
        List<Producto> productos = new ArrayList<>();
        List<LineaDePedido> lineas = new ArrayList<>();
        for (ComandoCrearPedido.Item item : comando.items()) {
            Producto producto = catalogo.buscarPorSku(item.sku())
                    .orElseThrow(() -> new ProductoNoEncontradoException(item.sku()));
            if (!producto.puedeSurtir(item.cantidad())) {
                throw new StockInsuficienteException(item.sku(), item.cantidad(), producto.stock());
            }
            productos.add(producto);
            lineas.add(new LineaDePedido(item.sku(), item.cantidad(), producto.precioUnitario()));
        }
        Pedido pedido = Pedido.crear(UUID.randomUUID().toString(), comando.cliente(), lineas);

        // 2. Descontar y persistir.
        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            producto.descontarStock(lineas.get(i).cantidad());
            catalogo.actualizar(producto);
        }
        return pedidos.guardar(pedido);
    }
}
