package co.edu.sabana.inventarium.domain.model;

import co.edu.sabana.inventarium.domain.exception.PedidoInvalidoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Prueba unitaria del dominio: Java puro, sin Spring, milisegundos. */
class PedidoTest {

    @Test
    void calculaElTotalSumandoLosSubtotales() {
        Pedido pedido = Pedido.crear("P-1", "Tienda La 80", List.of(
                new LineaDePedido("INV-TV-55", 2, new BigDecimal("2899000")),
                new LineaDePedido("INV-CBL-HD", 3, new BigDecimal("45000"))));

        assertThat(pedido.total()).isEqualByComparingTo("5933000");
        assertThat(pedido.estado()).isEqualTo(EstadoPedido.CREADO);
    }

    @Test
    void rechazaUnPedidoSinLineas() {
        assertThatThrownBy(() -> Pedido.crear("P-2", "Tienda La 80", List.of()))
                .isInstanceOf(PedidoInvalidoException.class);
    }

    @Test
    void rechazaUnPedidoSinCliente() {
        List<LineaDePedido> lineas = List.of(new LineaDePedido("INV-TV-55", 1, BigDecimal.TEN));

        assertThatThrownBy(() -> Pedido.crear("P-3", " ", lineas))
                .isInstanceOf(PedidoInvalidoException.class);
    }

    @Test
    void rechazaUnSkuRepetidoEnDosLineas() {
        List<LineaDePedido> lineas = List.of(
                new LineaDePedido("INV-TV-55", 1, BigDecimal.TEN),
                new LineaDePedido("INV-TV-55", 2, BigDecimal.TEN));

        assertThatThrownBy(() -> Pedido.crear("P-4", "Tienda La 80", lineas))
                .isInstanceOf(PedidoInvalidoException.class);
    }

    @Test
    void rechazaCantidadesNoPositivas() {
        assertThatThrownBy(() -> new LineaDePedido("INV-TV-55", 0, BigDecimal.TEN))
                .isInstanceOf(PedidoInvalidoException.class);
    }

    @Test
    void noSeDespachaDosVeces() {
        Pedido pedido = Pedido.crear("P-5", "Tienda La 80",
                List.of(new LineaDePedido("INV-TV-55", 1, BigDecimal.TEN)));
        pedido.despachar();

        assertThat(pedido.estado()).isEqualTo(EstadoPedido.DESPACHADO);
        assertThatThrownBy(pedido::despachar).isInstanceOf(PedidoInvalidoException.class);
    }
}
