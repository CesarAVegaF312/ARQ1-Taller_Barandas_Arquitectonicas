package co.edu.sabana.inventarium.delivery.rest;

import co.edu.sabana.inventarium.domain.model.PoliticaDeStockBajo;
import co.edu.sabana.inventarium.domain.model.Producto;
import co.edu.sabana.inventarium.infrastructure.persistence.CatalogoEnMemoriaAdaptador;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StockBajoControllerTest {

    @Test
    void devuelveSoloLosProductosConStockBajoOrdenadosDeMenorAMayor() {
        CatalogoEnMemoriaAdaptador catalogo = new CatalogoEnMemoriaAdaptador(List.of(
                new Producto("INV-TV-55", "Televisor", BigDecimal.TEN, 12),
                new Producto("INV-LAP-14", "Portatil", BigDecimal.TEN, 4),
                new Producto("INV-AUD-01", "Audifonos", BigDecimal.TEN, 3)));
        StockBajoController controller = new StockBajoController(catalogo, new PoliticaDeStockBajo(5));

        List<Map<String, Object>> respuesta = controller.stockBajo();

        assertThat(respuesta).extracting(f -> f.get("sku")).containsExactly("INV-AUD-01", "INV-LAP-14");
        assertThat(respuesta.get(0)).containsEntry("umbral", 5);
    }

    @Test
    void devuelveVacioSiTodoEstaSurtido() {
        CatalogoEnMemoriaAdaptador catalogo = new CatalogoEnMemoriaAdaptador(List.of(
                new Producto("INV-TV-55", "Televisor", BigDecimal.TEN, 12)));
        StockBajoController controller = new StockBajoController(catalogo, new PoliticaDeStockBajo(5));

        assertThat(controller.stockBajo()).isEmpty();
    }
}
