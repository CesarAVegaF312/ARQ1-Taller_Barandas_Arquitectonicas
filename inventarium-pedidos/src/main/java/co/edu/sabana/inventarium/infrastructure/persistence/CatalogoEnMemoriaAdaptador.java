package co.edu.sabana.inventarium.infrastructure.persistence;

import co.edu.sabana.inventarium.application.port.out.CatalogoDeProductos;
import co.edu.sabana.inventarium.domain.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Adaptador secundario con un catalogo de ejemplo de la distribuidora. */
@Component
public class CatalogoEnMemoriaAdaptador implements CatalogoDeProductos {

    private final Map<String, Producto> productos = new ConcurrentHashMap<>();

    @Autowired
    public CatalogoEnMemoriaAdaptador() {
        this(List.of(
                new Producto("INV-TV-55", "Televisor 55 pulgadas 4K", new BigDecimal("2899000"), 12),
                new Producto("INV-LAP-14", "Portatil 14 pulgadas 16 GB", new BigDecimal("3450000"), 4),
                new Producto("INV-CEL-A5", "Celular gama media 128 GB", new BigDecimal("1199000"), 30),
                new Producto("INV-AUD-01", "Audifonos inalambricos", new BigDecimal("289000"), 3),
                new Producto("INV-CBL-HD", "Cable HDMI 2.1 de 2 m", new BigDecimal("45000"), 120)));
    }

    /** Para pruebas: arranca con el catalogo que se le entregue. */
    public CatalogoEnMemoriaAdaptador(List<Producto> iniciales) {
        iniciales.forEach(p -> productos.put(p.sku(), p));
    }

    @Override
    public Optional<Producto> buscarPorSku(String sku) {
        return Optional.ofNullable(productos.get(sku));
    }

    @Override
    public void actualizar(Producto producto) {
        productos.put(producto.sku(), producto);
    }
}
