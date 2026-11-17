package co.edu.sabana.inventarium.config;

import co.edu.sabana.inventarium.application.port.in.ConsultarPedidoCasoDeUso;
import co.edu.sabana.inventarium.application.port.in.CrearPedidoCasoDeUso;
import co.edu.sabana.inventarium.application.port.out.CatalogoDeProductos;
import co.edu.sabana.inventarium.application.port.out.RepositorioDePedidos;
import co.edu.sabana.inventarium.application.usecase.ConsultarPedidoServicio;
import co.edu.sabana.inventarium.application.usecase.CrearPedidoServicio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Raiz de composicion: el unico lugar que conoce a la vez los puertos y sus
 * implementaciones. Gracias a esto, application/ no necesita importar Spring.
 */
@Configuration
public class ConfiguracionCasosDeUso {

    @Bean
    CrearPedidoCasoDeUso crearPedidoCasoDeUso(CatalogoDeProductos catalogo, RepositorioDePedidos pedidos) {
        return new CrearPedidoServicio(catalogo, pedidos);
    }

    @Bean
    ConsultarPedidoCasoDeUso consultarPedidoCasoDeUso(RepositorioDePedidos pedidos) {
        return new ConsultarPedidoServicio(pedidos);
    }
}
