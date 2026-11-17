package co.edu.sabana.inventarium.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PoliticaDeStockBajoTest {

    private final PoliticaDeStockBajo politica = new PoliticaDeStockBajo(5);

    @Test
    void aplicaCuandoElStockEstaEnElUmbralOPorDebajo() {
        assertThat(politica.aplicaA(producto(5))).isTrue();
        assertThat(politica.aplicaA(producto(0))).isTrue();
    }

    @Test
    void noAplicaPorEncimaDelUmbral() {
        assertThat(politica.aplicaA(producto(6))).isFalse();
    }

    private static Producto producto(int stock) {
        return new Producto("SKU-" + stock, "Producto", BigDecimal.ONE, stock);
    }
}
