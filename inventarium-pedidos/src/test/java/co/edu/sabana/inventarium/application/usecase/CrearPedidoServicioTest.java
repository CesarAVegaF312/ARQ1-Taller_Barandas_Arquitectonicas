package co.edu.sabana.inventarium.application.usecase;

import co.edu.sabana.inventarium.application.port.in.ComandoCrearPedido;
import co.edu.sabana.inventarium.application.port.out.CatalogoDeProductos;
import co.edu.sabana.inventarium.application.port.out.RepositorioDePedidos;
import co.edu.sabana.inventarium.domain.exception.ProductoNoEncontradoException;
import co.edu.sabana.inventarium.domain.exception.StockInsuficienteException;
import co.edu.sabana.inventarium.domain.model.Pedido;
import co.edu.sabana.inventarium.domain.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Prueba unitaria del caso de uso. Los puertos secundarios se reemplazan por
 * dobles: el caso de uso no sabe ni le importa que haya detras.
 */
class CrearPedidoServicioTest {

    private CatalogoDeProductos catalogo;
    private RepositorioDePedidos pedidos;
    private CrearPedidoServicio servicio;

    @BeforeEach
    void preparar() {
        catalogo = mock(CatalogoDeProductos.class);
        pedidos = mock(RepositorioDePedidos.class);
        when(pedidos.guardar(any())).thenAnswer(inv -> inv.getArgument(0));
        servicio = new CrearPedidoServicio(catalogo, pedidos);
    }

    @Test
    void creaElPedidoYDescuentaElStock() {
        Producto tv = new Producto("INV-TV-55", "Televisor", new BigDecimal("2899000"), 12);
        when(catalogo.buscarPorSku("INV-TV-55")).thenReturn(Optional.of(tv));

        Pedido pedido = servicio.crear(new ComandoCrearPedido("Tienda La 80",
                List.of(new ComandoCrearPedido.Item("INV-TV-55", 2))));

        assertThat(pedido.total()).isEqualByComparingTo("5798000");
        assertThat(tv.stock()).isEqualTo(10);
        verify(catalogo).actualizar(tv);
        verify(pedidos).guardar(pedido);
    }

    @Test
    void siUnaLineaNoTieneStockNoSeDescuentaNinguna() {
        Producto tv = new Producto("INV-TV-55", "Televisor", new BigDecimal("2899000"), 12);
        Producto audifonos = new Producto("INV-AUD-01", "Audifonos", new BigDecimal("289000"), 3);
        when(catalogo.buscarPorSku("INV-TV-55")).thenReturn(Optional.of(tv));
        when(catalogo.buscarPorSku("INV-AUD-01")).thenReturn(Optional.of(audifonos));

        ComandoCrearPedido comando = new ComandoCrearPedido("Tienda La 80", List.of(
                new ComandoCrearPedido.Item("INV-TV-55", 1),
                new ComandoCrearPedido.Item("INV-AUD-01", 5)));

        assertThatThrownBy(() -> servicio.crear(comando)).isInstanceOf(StockInsuficienteException.class);
        assertThat(tv.stock()).isEqualTo(12);
        verify(catalogo, never()).actualizar(any());
        verify(pedidos, never()).guardar(any());
    }

    @Test
    void fallaSiElProductoNoExiste() {
        when(catalogo.buscarPorSku("NO-EXISTE")).thenReturn(Optional.empty());

        ComandoCrearPedido comando = new ComandoCrearPedido("Tienda La 80",
                List.of(new ComandoCrearPedido.Item("NO-EXISTE", 1)));

        assertThatThrownBy(() -> servicio.crear(comando)).isInstanceOf(ProductoNoEncontradoException.class);
    }
}
