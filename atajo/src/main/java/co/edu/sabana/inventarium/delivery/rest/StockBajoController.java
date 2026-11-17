package co.edu.sabana.inventarium.delivery.rest;

import co.edu.sabana.inventarium.domain.model.PoliticaDeStockBajo;
import co.edu.sabana.inventarium.domain.model.Producto;
import co.edu.sabana.inventarium.infrastructure.persistence.CatalogoEnMemoriaAdaptador;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Alerta de stock bajo: GET /inventario/stock-bajo devuelve los productos
 * cuyo stock es menor o igual al umbral configurado.
 */
@RestController
public class StockBajoController {

    private final CatalogoEnMemoriaAdaptador catalogo;
    private final PoliticaDeStockBajo politica;

    public StockBajoController(CatalogoEnMemoriaAdaptador catalogo, PoliticaDeStockBajo politica) {
        this.catalogo = catalogo;
        this.politica = politica;
    }

    @GetMapping("/inventario/stock-bajo")
    public List<Map<String, Object>> stockBajo() {
        return catalogo.todos().stream()
                .filter(politica::aplicaA)
                .sorted(Comparator.comparingInt(Producto::stock))
                .map(p -> {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    fila.put("sku", p.sku());
                    fila.put("nombre", p.nombre());
                    fila.put("stock", p.stock());
                    fila.put("umbral", politica.umbral());
                    return fila;
                })
                .toList();
    }
}
