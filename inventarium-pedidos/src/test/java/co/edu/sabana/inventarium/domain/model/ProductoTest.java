package co.edu.sabana.inventarium.domain.model;

import co.edu.sabana.inventarium.domain.exception.StockInsuficienteException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductoTest {

    @Test
    void descuentaStockCuandoAlcanza() {
        Producto portatil = new Producto("INV-LAP-14", "Portatil", new BigDecimal("3450000"), 4);

        portatil.descontarStock(3);

        assertThat(portatil.stock()).isEqualTo(1);
    }

    @Test
    void noDescuentaMasDeLoQueHay() {
        Producto portatil = new Producto("INV-LAP-14", "Portatil", new BigDecimal("3450000"), 4);

        assertThatThrownBy(() -> portatil.descontarStock(5))
                .isInstanceOf(StockInsuficienteException.class)
                .hasMessageContaining("INV-LAP-14");
        assertThat(portatil.stock()).isEqualTo(4);
    }
}
