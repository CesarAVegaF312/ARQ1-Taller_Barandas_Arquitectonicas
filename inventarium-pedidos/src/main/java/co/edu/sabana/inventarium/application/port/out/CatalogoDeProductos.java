package co.edu.sabana.inventarium.application.port.out;

import co.edu.sabana.inventarium.domain.model.Producto;

import java.util.Optional;

/** Puerto secundario (driven) para leer y actualizar el catalogo y su stock. */
public interface CatalogoDeProductos {

    Optional<Producto> buscarPorSku(String sku);

    void actualizar(Producto producto);
}
